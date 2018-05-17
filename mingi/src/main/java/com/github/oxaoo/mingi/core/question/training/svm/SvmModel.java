package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_model;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
public interface SvmModel {

    svm_model getModel();
}
