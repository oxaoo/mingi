package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.FindSvmModelException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.qas.business.logic.exceptions.SaveSvmModelException;
import com.github.oxaoo.qas.business.logic.training.SvmEngine;
import com.github.oxaoo.qas.business.logic.training.TrainerQuestionClassifier;
import com.github.oxaoo.qas.business.logic.utils.PropertyManager;
import libsvm.svm_model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifierModelLoader {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionClassifierModelLoader.class);

    private final static String QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY = "question.classifier.model.path";
    private final static String QAS_HOME_PROPERTY = "qas.home";

    public static svm_model load() throws LoadQuestionClassifierModelException {
        String modelPath = PropertyManager.getProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        try {
            return SvmEngine.findModel(modelPath);
        } catch (FindSvmModelException e) {
            LOG.info("The question classifier model wasn't found!");
            try {
                return build();
            } catch (FailedParsingException
                    | FailedConllMapException
                    | FailedQuestionTokenMapException
                    | ReadInputTextException
                    | ProvideParserException
                    | SaveSvmModelException e1) {
                throw new LoadQuestionClassifierModelException("Failed to load question classifier model.", e1);
            }
        }
    }

    private static svm_model build() throws ProvideParserException,
            FailedParsingException,
            FailedConllMapException,
            ReadInputTextException,
            FailedQuestionTokenMapException,
            SaveSvmModelException {
        LOG.info("Building the question classifier model...");
        String modelPath = PropertyManager.getProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        TrainerQuestionClassifier trainer = new TrainerQuestionClassifier();
        svm_model model = trainer.build();
        String absoluteModelPath;
        try {
            absoluteModelPath = apply2Home(modelPath);
        } catch (ResourceResolverException e) {
            throw new SaveSvmModelException("Could not get the full path to the model store directory.", e);
        }
        SvmEngine.saveModel(model, absoluteModelPath);
        return model;
    }

    private static String apply2Home(String path) throws ResourceResolverException {
        String qasHome = PropertyManager.getProperty(QAS_HOME_PROPERTY);
        String qasAbsoluteHome = ResourceResolver.getAbsolutePath(qasHome);
        String fileName = path.replace(qasHome, "");
        return qasAbsoluteHome + '/' + fileName;
    }
}
