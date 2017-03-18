package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.training.SvmEngine;
import com.github.oxaoo.qas.utils.PropertyManager;
import libsvm.svm_model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifierModelLoader {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionClassifierModelLoader.class);

    private final static String QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY = "question.classifier.model.path";

    public svm_model load() {
        String modelPath = PropertyManager.getProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        try {
            return SvmEngine.loadModel(modelPath);
        } catch (IOException e) {
            LOG.info("The question classifier model wasn't found!");
            return this.build();
        }
    }


    private svm_model build() {
        LOG.info("Building the question classifier model...");
        return null;
    }
}
