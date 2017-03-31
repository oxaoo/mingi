package com.github.oxaoo.qas.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
public class ParseGraphBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ParseGraphBuilder.class);

    private enum NodeRelation {
        PARENT,
        CHILD
    }

    public static ParseGraph<Conll> makeFuture(List<Conll> conlls) {
        ParseGraph<Conll> graph = new ParseGraph<>();
        GraphComparator<Conll> relativeComparator = new RelativeConllNodeComparator();
        for (Conll conll : conlls) {
            ParseNode<Conll> relativeNode = graph.find(conll, relativeComparator);
            if (relativeNode == null) {
                graph.addNewTree(conll);
            } else {
                NodeRelation relation = getRelation(conll, relativeNode.getValue());
                if (relation == NodeRelation.PARENT) graph.addNodeAsParent(conll, relativeNode);
                else graph.addNodeAsChild(conll, relativeNode);
            }
        }
        mergeConllGraphForest(graph);
        return graph;
    }

    private static void mergeConllGraphForest(ParseGraph<Conll> graph) {
        List<ParseNode<Conll>> forest = graph.getForest();
        Iterator<ParseNode<Conll>> it = forest.iterator();
        while (it.hasNext()) {
            ParseNode<Conll> node = it.next();
            if (node.getValue().getHead() == 0) continue;
            ParseNode<Conll> parent = graph.findParent(node);
            if (parent != null) {
                parent.addChild(node);
                it.remove();
            }
        }
    }

    private static NodeRelation getRelation(Conll current, Conll related) {
        if (current.getHead() == related.getId()) return NodeRelation.CHILD;
        else if (current.getId() == related.getHead()) return NodeRelation.PARENT;
        else {
            throw new IllegalStateException("There is no relationship between #"
                    + current.getId() + " and #" + related.getId());
        }
    }

    public static ParseGraph<Conll> make(List<Conll> conlls) {
        conlls = conlls.stream().sorted(Comparator.comparingInt(Conll::getHead)).collect(Collectors.toList());
        regulate(conlls);
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
                    if (parentNode == null) {
                        continue;
                    }
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

    private static void regulate(List<Conll> conlls) {
        for (int i = 0; i < conlls.size() - 1; i++) {
            Conll cur = conlls.get(i);
            for (int j = i + 1; j < conlls.size() - 1; j++) {
                Conll seq = conlls.get(j);
                if (cur.getHead() == seq.getId()) {
                    //then swap
                    conlls.set(i, seq);
                    conlls.set(j, cur);
                    break;
                }
            }
        }
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
