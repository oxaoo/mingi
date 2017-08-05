package com.github.oxaoo.qas.business.logic.qa.answer.location;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.parse.ConllGraphComparator;
import com.github.oxaoo.qas.business.logic.parse.ConllParseGraphBuilder;
import com.github.oxaoo.qas.business.logic.parse.ParseGraph;
import com.github.oxaoo.qas.business.logic.parse.ParseNode;
import com.github.oxaoo.qas.business.logic.qa.answer.AnswerMaker;
import com.github.oxaoo.qas.business.logic.qa.answer.AnswerMakerTools;
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
public class StateAnswerMaker extends LocationAnswerMaker<String, Conll, DataFragment> {
    @Override
    public List<Callable<String>> toAnswer(List<Conll> tokens, List<DataFragment> data) {
        Conll headQuestionToken = AnswerMakerTools.getRoot(tokens);
        List<String> sentences = AnswerMakerTools.getSentences(data);
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
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByPosAndDep(foundNode, 'N', "предл");
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

    //todo make dep as constant!
    private Set<ParseNode<Conll>> findPath2ChildByPosAndDep(ParseNode<Conll> parent, char pos, String dep) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByPosAndDep(parent, pos, dep, answerChain);
        return answerChain;
    }

    private boolean findByPosAndDep(ParseNode<Conll> node, char pos, String dep, Set<ParseNode<Conll>> chain) {
        if (node.getValue().getPosTag() == pos
                && dep.equals(node.getValue().getDepRel())) {
            chain.addAll(node.getAllChild());
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByPosAndDep(child, pos, dep, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }
}
