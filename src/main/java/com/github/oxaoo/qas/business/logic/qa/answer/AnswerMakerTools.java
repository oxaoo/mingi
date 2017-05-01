package com.github.oxaoo.qas.business.logic.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.parse.ParseNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 09.04.2017
 */
public class AnswerMakerTools {

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

    public static Conll getRoot(List<Conll> tokens) {
        return tokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .findFirst()
                .orElse(null);
    }

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
}
