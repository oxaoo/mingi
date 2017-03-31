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

    public void addNewTree(T t) {
        forest.add(new ParseNode<T>(this, t));
    }
}
