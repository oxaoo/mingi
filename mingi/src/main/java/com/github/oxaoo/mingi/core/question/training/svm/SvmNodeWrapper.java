package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_node;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
@Getter
@AllArgsConstructor
public class SvmNodeWrapper implements SvmNode {
    private final svm_node node;

    public SvmNodeWrapper(final int index, final double value) {
        this.node = new svm_node();
        this.node.index = index;
        this.node.value = value;
    }

    @Override
    public void setIndex(final int index) {
        this.node.index = index;
    }

    @Override
    public int getIndex() {
        return node.index;
    }

    @Override
    public void setValue(final double value) {
        this.node.value = value;
    }

    @Override
    public double getValue() {
        return this.node.value;
    }
}
