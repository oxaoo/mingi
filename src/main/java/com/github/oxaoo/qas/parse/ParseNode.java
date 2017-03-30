package com.github.oxaoo.qas.parse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ParseNode<T> {
    @NonNull
    private final ParseGraph graph;
    @NonNull
    private final T value;
    private ParseNode<T> parent;
    private ParseNode<T> lhs;
    private ParseNode<T> rhs;

    public void addChild(ParseNode<T> child) {
        if (lhs == null) {
            lhs = child;
        } else if (rhs == null) {
            rhs = child;
        } else {
            throw new IndexOutOfBoundsException("The node already has two children.");
        }
    }

    public List<ParseNode<T>> getChild() {
        List<ParseNode<T>> child = new ArrayList<>();
        if (this.lhs != null) child.add(this.lhs);
        if (this.rhs != null) child.add(this.rhs);
        return child;
    }

    public List<ParseNode<T>> getAllChild() {
        List<ParseNode<T>> child = new ArrayList<>();
        child.add(this);
        this.getAllChild(this, child);
        return child;
    }

    private void getAllChild(ParseNode<T> currentNode, List<ParseNode<T>> child) {
        if (currentNode.lhs != null) {
            child.add(currentNode.lhs);
            this.getAllChild(currentNode.lhs, child);
        }
        if (currentNode.rhs != null) {
            child.add(currentNode.rhs);
            this.getAllChild(currentNode.rhs, child);
        }
    }
}
