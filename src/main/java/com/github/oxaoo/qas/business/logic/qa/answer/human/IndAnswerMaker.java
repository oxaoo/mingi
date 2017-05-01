package com.github.oxaoo.qas.business.logic.qa.answer.human;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.parse.ConllGraphComparator;
import com.github.oxaoo.qas.business.logic.parse.ConllParseGraphBuilder;
import com.github.oxaoo.qas.business.logic.parse.ParseGraph;
import com.github.oxaoo.qas.business.logic.parse.ParseNode;
import com.github.oxaoo.qas.business.logic.search.data.DataFragment;
import com.github.oxaoo.qas.business.logic.search.data.RelevantInfo;

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
public class IndAnswerMaker extends HumanAnswerMaker<String, Conll, DataFragment> {

    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        tokens = tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        Conll headQuestionToken = tokens.get(0);

        List<String> sentences = data.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());

        return sentences.stream()
                .map(s -> (Callable<String>) () -> this.answer(s, headQuestionToken))
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
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByStartFeats(foundNode, "Np");
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

    private Set<ParseNode<Conll>> findPath2ChildByStartFeats(ParseNode<Conll> parent, String startFeats) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByFeats(parent, startFeats, answerChain);
        return answerChain;
    }

    private boolean findByFeats(ParseNode<Conll> node, String startFeats, Set<ParseNode<Conll>> chain) {
        if (node.getValue().getFeats().startsWith(startFeats)) {
            chain.addAll(node.getAllChild());
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByFeats(child, startFeats, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }
}
