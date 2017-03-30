package com.github.oxaoo.qas.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
public class ParseGraphBuilder {

    public static ParseGraph<Conll> make(List<Conll> conlls) {
        conlls = conlls.stream().sorted(Comparator.comparingInt(Conll::getHead)).collect(Collectors.toList());
        ParseGraph<Conll> graph = new ParseGraph<>();
        List<ParseNode<Conll>> forest = new ArrayList<>();
        ParseNode<Conll> lastNode = null;
        for (Conll conll : conlls) {
            if (conll.getHead() == 0) {
                //optimization
                lastNode = new ParseNode<>(graph, conll);
                forest.add(lastNode);
            } else {
                ParseNode<Conll> newNode = new ParseNode<>(graph, conll);
                final ParseNode<Conll> parentNode;
                if (lastNode.getValue().getId() == conll.getHead()) {
                    parentNode = lastNode;
                } else {
                    parentNode = getParent(forest, conll.getHead());
                    //fixme -> temporary feature
                    if (parentNode == null) continue;
                }
                newNode.setParent(parentNode);
                parentNode.addChild(newNode);
                forest.add(newNode);
                lastNode = newNode;
            }
        }
        graph.setForest(forest);
        return graph;
    }

    private static ParseNode<Conll> getParent(List<ParseNode<Conll>> forest, int id) {
        for (ParseNode<Conll> tree : forest) {
            ParseNode<Conll> parent = getParent(tree, id);
            if (parent != null) return parent;
        }
        //todo create custom exception
        //fixme -> temporary feature
        return null;
//        throw new RuntimeException("Not found parent node.");
    }

    //get parent by id
    private static ParseNode<Conll> getParent(ParseNode<Conll> node, int id) {
        if (node == null) return null;
        if (node.getValue().getId() == id) return node;
        for (ParseNode<Conll> child : node.getChildren()) {
            ParseNode<Conll> foundParent = getParent(child, id);
            if (foundParent != null) return foundParent;
        }
//        ParseNode<Conll> parentFromLeft = getParent(node.getLhs(), id);
//        if (parentFromLeft != null) return parentFromLeft;
//        ParseNode<Conll> parentFromRight = getParent(node.getRhs(), id);
//        if (parentFromRight != null) return parentFromRight;
        return null;
    }
}
