package com.github.oxaoo.mingi.core.question.training.svm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 12.05.2018
 */
@Getter
@AllArgsConstructor
public enum SvmKernelType {
    LINEAR(0),
    POLY(1),
    RBF(2),
    SIGMOID(3),
    PRECOMPUTED(4);

    private final int index;
}
