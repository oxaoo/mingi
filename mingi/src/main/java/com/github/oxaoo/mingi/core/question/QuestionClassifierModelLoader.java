package com.github.oxaoo.mingi.core.question;

import com.github.oxaoo.mingi.core.question.training.svm.SvmModel;
import com.github.oxaoo.mingi.exceptions.BuildModelException;
import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;

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
    SvmModel load(String qasHome) throws LoadQuestionClassifierModelException;

    /**
     * Build svm model.
     *
     * @return the svm model
     * @throws BuildModelException the build model exception
     */
    SvmModel build(String qasHome) throws BuildModelException;
}
