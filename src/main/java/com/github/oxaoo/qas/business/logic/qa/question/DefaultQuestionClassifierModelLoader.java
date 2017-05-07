package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.qas.business.logic.common.PropertyManager;
import com.github.oxaoo.qas.business.logic.exceptions.BuildModelException;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.FindSvmModelException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.qas.business.logic.exceptions.SaveSvmModelException;
import com.github.oxaoo.qas.business.logic.training.SvmEngine;
import com.github.oxaoo.qas.business.logic.training.TrainerQuestionClassifier;
import libsvm.svm_model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
@Getter
@Setter
@AllArgsConstructor
public class DefaultQuestionClassifierModelLoader implements QuestionClassifierModelLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultQuestionClassifierModelLoader.class);

    final static String QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY = "question.classifier.model.path";
    final static String QAS_HOME_PROPERTY = "qas.home";

    private final SvmEngine svmEngine;

    public DefaultQuestionClassifierModelLoader() throws ProvideParserException {
        this.svmEngine = new SvmEngine();
    }

    @Override
    public svm_model load() throws LoadQuestionClassifierModelException {
        String modelPath = PropertyManager.getProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        try {
            return this.svmEngine.findModel(modelPath);
        } catch (FindSvmModelException e) {
            LOG.info("The question classifier model wasn't found!");
            try {
                return this.build();
            } catch (BuildModelException e1) {
                throw new LoadQuestionClassifierModelException("Failed to load the question classifier model.", e1);
            }
        }
    }

    @Override
    public svm_model build() throws BuildModelException {
        LOG.info("Building the question classifier model...");
        try {
            String modelPath = PropertyManager.getProperty(QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
            //todo make trainer as field
            TrainerQuestionClassifier trainer = new TrainerQuestionClassifier();
            svm_model model = trainer.build();
            String absoluteModelPath;
            try {
                absoluteModelPath = this.apply2Home(modelPath);
            } catch (ResourceResolverException e) {
                throw new SaveSvmModelException("Could not get the full path to the model store directory.", e);
            }
            this.svmEngine.saveModel(model, absoluteModelPath);
            return model;
        } catch (SaveSvmModelException
                | FailedParsingException
                | ProvideParserException
                | FailedQuestionTokenMapException
                | ReadInputTextException
                | FailedConllMapException e) {
            throw new BuildModelException("Failed to build the question classifier model.", e);
        }
    }

    String apply2Home(String path) throws ResourceResolverException {
        String qasHome = PropertyManager.getProperty(QAS_HOME_PROPERTY);
        String qasAbsoluteHome = ResourceResolver.getAbsolutePath(qasHome);
        String fileName = path.replace(qasHome, "");
        return qasAbsoluteHome + '/' + fileName;
    }
}
