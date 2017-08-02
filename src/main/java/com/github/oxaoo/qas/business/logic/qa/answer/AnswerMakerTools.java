package com.github.oxaoo.qas.business.logic.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.parse.ParseNode;
import com.github.oxaoo.qas.business.logic.search.data.DataFragment;
import com.github.oxaoo.qas.business.logic.search.data.RelevantInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 09.04.2017
 */
public class AnswerMakerTools {

    public static List<String> getSentences(List<DataFragment> data) {
        return data.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static ParseNode<Conll> findFirstAfter(ParseNode<Conll> target, String featsPattern) {
        List<ParseNode<Conll>> subChildren = new ArrayList<>();
        for (ParseNode<Conll> childNode : target.getChildren()) {
            if (childNode.getValue().getFeats().matches(featsPattern)) return childNode;
            else subChildren.addAll(childNode.getAllChild());
        }
        for (ParseNode<Conll> subChildNode : subChildren) {
            ParseNode<Conll> foundNode = findFirstAfter(subChildNode, featsPattern);
            if (foundNode != null) return foundNode;
        }
        return null;
    }

    public static Conll findFirstAfterRoot(List<Conll> tokens, String featsPattern) {
        tokens = tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        for (int i = 1; i < tokens.size(); i++) {
            if (tokens.get(i).getFeats().matches(featsPattern)) return tokens.get(i);
        }
        //default variant
        return tokens.get(0);
    }

    public static Conll findSecondAfterRoot(List<Conll> tokens, String featsPattern) {
        tokens = tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        boolean isFoundFirst = false;
        Conll firstNode = null;
        for (int i = 1; i < tokens.size(); i++) {
            if (tokens.get(i).getFeats().matches(featsPattern)) {
                if (isFoundFirst) return tokens.get(i);
                else {
                    isFoundFirst = true;
                    firstNode = tokens.get(i);
                }
            }
        }

        if (firstNode != null) return firstNode;
        //default variant
        return tokens.get(0);
    }

    /**
     * Gets root.
     * Sorts all conlls by the parent's id and returns the first found-with an id = 0.
     *
     * @param tokens the conll tokens
     * @return the conll root which has head id = 0
     */
    public static Conll getRoot(List<Conll> tokens) {
        return tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .findFirst().get();
    }

    /**
     * Gets root.
     * Sorts all conlls by the parent's id and returns the first found-with a feats pattern.
     *
     * @param tokens       the tokens
     * @param featsPattern the feats pattern
     * @return the conll root which has matches by feats pattern or <tt>null</tt>
     */
    public static Conll getRoot(List<Conll> tokens, String featsPattern) {
        return tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .filter(c -> c.getFeats().matches(featsPattern))
                .findFirst()
                .orElse(null);
    }

    public static ParseNode<Conll> getFirstParent(final ParseNode<Conll> target, String featsPattern) {
        ParseNode<Conll> current = target;
        while (current.getParent() != null) {
            ParseNode<Conll> parent = current.getParent();
            if (parent.getValue().getFeats().matches(featsPattern)) return parent;
            else current = parent;
        }

        //default variant
        return target;
    }

    /**
     * Gets unordered chain from the start node to the node with specific target POS.
     *
     * @param startNode the start node
     * @param targetPos the target pos
     * @return the chain to the node with target POS
     */
    public static Set<ParseNode<Conll>> getChain2Pos(ParseNode<Conll> startNode, char targetPos) {
        return AnswerMakerTools.getChain2Pos(startNode, Collections.singletonList(targetPos));
    }

    /**
     * Gets unordered chain from the start node to the node with specific target list of POS.
     *
     * @param startNode     the start node
     * @param targetPosList the target pos list
     * @return the chain to the node with target POS
     */
    public static Set<ParseNode<Conll>> getChain2Pos(ParseNode<Conll> startNode, List<Character> targetPosList) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        AnswerMakerTools.buildChain2Pos(startNode, targetPosList, answerChain);
        return answerChain;
    }


    /**
     * Building the chain from the start node to the child node with specific target list of POS.
     *
     * @param startNode     the start node
     * @param targetPosList the target pos list
     * @param chain         the chain
     * @return the boolean
     */
    private static boolean buildChain2Pos(ParseNode<Conll> startNode,
                                          List<Character> targetPosList,
                                          Set<ParseNode<Conll>> chain) {
        if (targetPosList.contains(startNode.getValue().getPosTag())) {
            //todo change the algo of adding context
            //added the dependent child nodes for completeness of the context.
            chain.addAll(startNode.getAllChild());
//            chain.addAll(AnswerMakerTools.getAllDescendants(startNode, new ArrayList<>()));
            return true;
        } else if (!startNode.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : startNode.getChildren()) {
                boolean isFound = buildChain2Pos(child, targetPosList, chain);
                if (isFound) {
                    chain.add(startNode);
                    return true;
                }
            }
        } else return false;
        return false;
    }

    /**
     * Prepare answer string.
     * Dependent nodes are sorting by an id and concat by a space.
     *
     * @param dependentNodes the set of unordered dependent nodes
     * @return the answer string
     */
    public static String prepareAnswer(Set<ParseNode<Conll>> dependentNodes) {
        StringBuilder sb = new StringBuilder();
        dependentNodes.stream()
                .map(ParseNode::getValue)
                .sorted(Comparator.comparingInt(Conll::getId))
                .forEach(c -> sb.append(c.getForm()).append(" "));
        return sb.toString();
    }

    /**
     * Gets all descendants of node.
     *
     * @param node       the node
     * @param childChain the child chain
     * @return the all descendants of node
     */
    private static List<ParseNode<Conll>> getAllDescendants(ParseNode<Conll> node, List<ParseNode<Conll>> childChain) {
        for (ParseNode<Conll> child : node.getChildren()) {
            childChain.add(child);
            AnswerMakerTools.getAllDescendants(child, childChain);
        }
        return childChain;
    }
}
