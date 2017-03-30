package com.github.oxaoo.qas.parse;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@ToString(exclude = "children")
public class ParseNode<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseNode.class);

    @NonNull
    private final ParseGraph graph;
    @NonNull
    private final T value;
    private ParseNode<T> parent;
    private final List<ParseNode<T>> children = new ArrayList<>();
//    private ParseNode<T> lhs;
//    private ParseNode<T> rhs;

    public void addChild(ParseNode<T> child) {
        this.children.add(child);
    }

    public List<ParseNode<T>> getAllChild() {
        List<ParseNode<T>> allChildren = new ArrayList<>();
        allChildren.add(this);
        this.getAllChild(this, allChildren);
        return allChildren;
    }

    private void getAllChild(ParseNode<T> currentNode, List<ParseNode<T>> allChildren) {
        for (ParseNode<T> child : currentNode.children) {
            allChildren.add(child);
            this.getAllChild(child, allChildren);
        }

//        if (currentNode.lhs != null) {
//            allChildren.add(currentNode.lhs);
//            this.getAllChild(currentNode.lhs, allChildren);
//        }
//        if (currentNode.rhs != null) {
//            allChildren.add(currentNode.rhs);
//            this.getAllChild(currentNode.rhs, allChildren);
//        }
    }
}
