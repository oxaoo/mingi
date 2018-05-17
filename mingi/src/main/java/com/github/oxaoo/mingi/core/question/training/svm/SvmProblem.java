package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_problem;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
public interface SvmProblem {

    svm_problem getProblem();
    void initMatrixRow(int capacity);
    void addMatrixValue(SvmNode node);
    void addVectorValue(double value);
//    void initXRow(int index, int size);
//    void setY(int index, double value);

}
