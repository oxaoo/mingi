package com.github.oxaoo.qas.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

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
//        for (ParseNode<T> node : queueNodes) {
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
//            if (child.getParent().equals(node)) return node;
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

    //use for merge short forest to three
//    public void merge(GraphComparator<T> comparator) {
//        Iterator<ParseNode<T>> it = this.forest.iterator();
//        while (it.hasNext()) {
//            ParseNode<T> curTree = it.next();
//            if (curTree.getValue())
//            Iterator<ParseNode<T>> subIt = this.forest.iterator();
//
//        }
//    }

//    public void addNewTree(ParseNode<T> node) {
//        forest.add(node);
//    }
}
