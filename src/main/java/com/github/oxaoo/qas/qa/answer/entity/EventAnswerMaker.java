package com.github.oxaoo.qas.qa.answer.entity;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.parse.ConllGraphComparator;
import com.github.oxaoo.qas.parse.ConllParseGraphBuilder;
import com.github.oxaoo.qas.parse.ParseGraph;
import com.github.oxaoo.qas.parse.ParseNode;
import com.github.oxaoo.qas.search.data.DataFragment;
import com.github.oxaoo.qas.search.data.RelevantInfo;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class EventAnswerMaker extends EntityAnswerMaker<String, Conll, DataFragment> {

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
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, Arrays.asList('N', 'V'));
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

    private Set<ParseNode<Conll>> findPath2ChildByPos(ParseNode<Conll> parent, List<Character> posList) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByPos(parent, posList, answerChain);
        return answerChain;
    }

    private boolean findByPos(ParseNode<Conll> node, List<Character> posList, Set<ParseNode<Conll>> chain) {
        if (posList.contains(node.getValue().getPosTag())) {
//            chain.addAll(node.getAllChild());
            chain.addAll(this.assemblyChainNode(node, new ArrayList<>()));
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByPos(child, posList, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }

    private List<ParseNode<Conll>> assemblyChainNode(ParseNode<Conll> node, List<ParseNode<Conll>> childChain) {
        for (ParseNode<Conll> child : node.getChildren()) {
            childChain.add(child);
            this.assemblyChainNode(child, childChain);
        }
        return childChain;
    }
}
