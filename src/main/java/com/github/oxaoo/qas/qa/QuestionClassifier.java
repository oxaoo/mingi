package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import libsvm.svm_model;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifier {
    private final svm_model model;

    public QuestionClassifier() throws LoadQuestionClassifierModelException {
        this.model = QuestionClassifierModelLoader.load();
    }

    //todo implement
    public QuestionDomain classify(String question) {
        return null;
    }
}
