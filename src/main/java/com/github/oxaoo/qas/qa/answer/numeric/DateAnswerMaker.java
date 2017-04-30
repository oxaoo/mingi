package com.github.oxaoo.qas.qa.answer.numeric;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.parse.ConllGraphComparator;
import com.github.oxaoo.qas.parse.ConllParseGraphBuilder;
import com.github.oxaoo.qas.parse.ParseGraph;
import com.github.oxaoo.qas.parse.ParseNode;
import com.github.oxaoo.qas.search.data.DataFragment;
import com.github.oxaoo.qas.search.data.RelevantInfo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class DateAnswerMaker extends NumericAnswerMaker<String, Conll, DataFragment> {

    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        tokens = tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        //find the verb
        final Conll targetToken;
        if (tokens.get(0).getPosTag() == 'V') {
            targetToken = tokens.get(0);
        } else {
            Conll foundToken = null;
            for (Conll token : tokens) {
                if (token.getPosTag() == 'V') {
                    foundToken = token;
                    break;
                }
            }
            if (foundToken != null) targetToken = foundToken;
            else targetToken = tokens.get(0);
        }

        List<String> sentences = data.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());

        return sentences.stream()
                .map(s -> (Callable<String>) () -> this.answer(s, targetToken))
                .collect(Collectors.toList());
    }

    private String answer(String sentence, Conll headQuestionToken)
            throws FailedParsingException {
        List<Conll> conlls = this.parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = new ConllParseGraphBuilder().build(conlls);
        ParseNode<Conll> foundNode = graph.find(headQuestionToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
//        List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, 'M');
        return prepareAnswer(dependentNodes);
    }


    private String prepareAnswer(Set<ParseNode<Conll>> dependentNodes) {
        StringBuilder sb = new StringBuilder();
        dependentNodes.stream()
                .map(ParseNode::getValue)
                .sorted(Comparator.comparingInt(Conll::getId))
                .forEach(c -> sb.append(c.getForm()).append(" "));
        return sb.toString();
    }

    private Set<ParseNode<Conll>> findPath2ChildByPos(ParseNode<Conll> parent, char pos) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByPos(parent, pos, answerChain);
//        Optional<ParseNode<Conll>> optionalNode
//                = answerChain.stream().filter(n -> n.getValue().getPosTag() == pos).findFirst();
//        if (optionalNode.isPresent()) {
//            ParseNode<Conll> foundNode = optionalNode.get();
//            findByPos(foundNode, pos, answerChain);
//        }
        return answerChain;
    }

    private boolean findByPos(ParseNode<Conll> node, char pos, Set<ParseNode<Conll>> chain) {
        if (node.getValue().getPosTag() == pos) {
            chain.addAll(node.getAllChild());
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByPos(child, pos, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }
}
