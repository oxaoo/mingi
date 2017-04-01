package com.github.oxaoo.qas.parse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseGraph<T> {
    private List<ParseNode<T>> forest = new ArrayList<>();

    public ParseNode<T> find(T wanted, GraphComparator<T> comparator) {
        Queue<ParseNode<T>> queueNodes = new LinkedList<>(this.forest);
        //todo check on bug
        while (true) {
            ParseNode<T> node = queueNodes.poll();
            if (node == null) break;
            T foundNodeValue = comparator.find(wanted, node.getValue());
            if (foundNodeValue != null) return node;
            else queueNodes.addAll(node.getChildren());
        }
        return null;
    }

    public ParseNode<T> findParent(ParseNode<T> child) {
        Queue<ParseNode<T>> queueNodes = new LinkedList<>(this.forest);
        while (true) {
            ParseNode<T> node = queueNodes.poll();
            if (node == null) break;
            if (node.equals(child.getParent())) return node;
            else queueNodes.addAll(node.getChildren());
        }
        return null;
    }

    public void addNewTree(T t) {
        forest.add(new ParseNode<>(this, t));
    }

    public void addNodeAsParent(T t, ParseNode<T> relativeNode) {
        ParseNode<T> parentNode = new ParseNode<>(this, t);
        parentNode.addChild(relativeNode);
        this.forest.add(parentNode);
    }

    public void addNodeAsChild(T t, ParseNode<T> relativeNode) {
        ParseNode<T> childNode = new ParseNode<>(this, t);
        relativeNode.addChild(childNode);
    }
}
