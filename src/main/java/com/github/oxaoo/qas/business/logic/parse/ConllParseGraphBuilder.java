package com.github.oxaoo.qas.business.logic.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

import java.util.*;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
public class ConllParseGraphBuilder extends ParseGraphBuilder<Conll> {

    public ConllParseGraphBuilder() {
        super();
    }

    public ConllParseGraphBuilder(ParseGraph<Conll> parseGraph) {
        super(parseGraph);
    }

    @Override
    public ParseGraph<Conll> build(Collection<Conll> conlls) {
        GraphComparator<Conll> relativeComparator = new RelativeConllNodeComparator();
        for (Conll conll : conlls) {
            ParseNode<Conll> relativeNode = this.parseGraph.find(conll, relativeComparator);
            if (relativeNode == null) {
                this.parseGraph.addNewTree(conll);
            } else {
                NodeRelation relation = getRelation(conll, relativeNode.getValue());
                if (relation == NodeRelation.PARENT) this.parseGraph.addNodeAsParent(conll, relativeNode);
                else this.parseGraph.addNodeAsChild(conll, relativeNode);
            }
        }
        mergeConllGraphForest(this.parseGraph);
        return this.parseGraph;
    }


    private enum NodeRelation {
        PARENT,
        CHILD
    }

    private void mergeConllGraphForest(ParseGraph<Conll> graph) {
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

    private ParseNode<Conll> findParent(ParseGraph<Conll> graph, ParseNode<Conll> child) {
        Queue<ParseNode<Conll>> queueNodes = new LinkedList<>(graph.getForest());
        while (true) {
            ParseNode<Conll> node = queueNodes.poll();
            if (node == null) break;
            if (node.getValue().getId() == child.getValue().getHead()) return node;
            else queueNodes.addAll(node.getChildren());
        }
        return null;
    }

    private NodeRelation getRelation(Conll current, Conll related) {
        if (current.getHead() == related.getId()) return NodeRelation.CHILD;
        else if (current.getId() == related.getHead()) return NodeRelation.PARENT;
        else {
            throw new IllegalStateException("There is no relationship between #"
                    + current.getId() + " and #" + related.getId());
        }
    }
}
