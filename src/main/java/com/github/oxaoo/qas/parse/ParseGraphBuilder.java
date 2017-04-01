package com.github.oxaoo.qas.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public static ParseGraph<Conll> make(List<Conll> conlls) {
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
//        LOG.info("GRAPH: {}", forest.toString());
        Iterator<ParseNode<Conll>> it = forest.iterator();
        while (it.hasNext()) {
            ParseNode<Conll> node = it.next();
            if (node.getValue().getHead() == 0) continue;
            ParseNode<Conll> parent = findParent(graph, node);
            if (parent != null) {
                parent.addChild(node);
                it.remove();
            }
        }
    }

    private static ParseNode<Conll> findParent(ParseGraph<Conll> graph, ParseNode<Conll> child) {
        Queue<ParseNode<Conll>> queueNodes = new LinkedList<>(graph.getForest());
        while (true) {
            ParseNode<Conll> node = queueNodes.poll();
            if (node == null) break;
            if (node.getValue().getId() == child.getValue().getHead()) return node;
            else queueNodes.addAll(node.getChildren());
        }
        return null;
    }

    private static NodeRelation getRelation(Conll current, Conll related) {
        if (current.getHead() == related.getId()) return NodeRelation.CHILD;
        else if (current.getId() == related.getHead()) return NodeRelation.PARENT;
        else {
            throw new IllegalStateException("There is no relationship between #"
                    + current.getId() + " and #" + related.getId());
        }
    }
}
