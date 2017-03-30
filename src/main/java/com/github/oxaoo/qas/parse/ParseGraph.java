package com.github.oxaoo.qas.parse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
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
        Queue<ParseNode<T>> queueNodes = new PriorityQueue<>(this.forest);
        //todo check on bug
        for (ParseNode<T> node : queueNodes) {
            T foundNodeValue = comparator.find(wanted, node.getValue());
            if (foundNodeValue != null) return node;
            else queueNodes.addAll(node.getChild());
        }
        return null;
    }
}
