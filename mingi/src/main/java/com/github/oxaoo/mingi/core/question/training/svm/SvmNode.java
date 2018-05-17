package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_node;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
public interface SvmNode {

    svm_node getNode();
    void setIndex(int index);
    int getIndex();
    void setValue(double value);
    double getValue();
}
