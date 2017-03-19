package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import libsvm.svm_model;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifier {
    private svm_model model;

    public void init() throws LoadQuestionClassifierModelException {
        this.model = QuestionClassifierModelLoader.load();
    }
}
