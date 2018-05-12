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
public enum SvmType {
    C_SVC(0),
    NU_SVC(1),
    ONE_CLASS(2),
    EPSILON_SVR(3),
    NU_SVR(4);

    private final int index;
}
