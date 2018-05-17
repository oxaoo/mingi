package com.github.oxaoo.mingi.core.question.training.svm;

import libsvm.svm_model;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
@Getter
@AllArgsConstructor
public class SvmModelWrapper implements SvmModel {

    private final svm_model model;
}
