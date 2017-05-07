package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.qas.business.logic.exceptions.BuildModelException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import libsvm.svm_model;

/**
 * The interface Question classifier model loader.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public interface QuestionClassifierModelLoader {
    /**
     * Load svm model.
     *
     * @return the svm model
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     */
    svm_model load() throws LoadQuestionClassifierModelException;

    /**
     * Build svm model.
     *
     * @return the svm model
     * @throws BuildModelException the build model exception
     */
    svm_model build() throws BuildModelException;
}
