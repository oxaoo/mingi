package com.github.oxaoo.mingi.core.question;

import com.github.oxaoo.mingi.core.question.training.TrainerQuestionClassifier;
import com.github.oxaoo.mingi.core.question.training.svm.SvmEngine;
import com.github.oxaoo.mingi.core.question.training.svm.SvmModel;
import com.github.oxaoo.mingi.exceptions.BuildModelException;
import com.github.oxaoo.mingi.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.exceptions.FindSvmModelException;
import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.exceptions.SaveSvmModelException;
import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.exceptions.ResourceResolverException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
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

    private static final String DEFAULT_QAS_HOME = "qas/";
    private static final String DEFAULT_QAS_MODEL_NAME = "qcm.model";

    private final RussianParser parser;
    private final SvmEngine svmEngine;

    @Override
    public SvmModel load(final String qasHome) throws LoadQuestionClassifierModelException {
        /*final String modelPath;
        try {
            modelPath = this.applyToHome(qasHome);
        } catch (final ResourceResolverException e) {
            throw new LoadQuestionClassifierModelException(
                    String.format("Failed to get the QAS home absolute path '%s'.", qasHome), e);
        }*/
        final String modelPath = (qasHome.endsWith("/") ? qasHome : qasHome + "/") + DEFAULT_QAS_MODEL_NAME;
        try {
            return this.svmEngine.findModel(modelPath);
        } catch (final FindSvmModelException e) {
            LOG.info(String.format("The question classifier model '%s' wasn't found!", modelPath));
            try {
                return this.build(qasHome);
            } catch (final BuildModelException e1) {
                throw new LoadQuestionClassifierModelException(
                        String.format("Failed to load the question classifier model '%s'.", modelPath), e1);
            }
        }
    }

    @Override
    public SvmModel build(final String qasHome) throws BuildModelException {
        LOG.info("Building the question classifier model...");
        try {
            //todo make trainer as field
            final TrainerQuestionClassifier trainer = new TrainerQuestionClassifier(qasHome, this.parser, this.svmEngine);
            final SvmModel model = trainer.build();
            final String absoluteModelPath;
            try {
                absoluteModelPath = this.applyToHome(qasHome);
            } catch (final ResourceResolverException e) {
                throw new SaveSvmModelException("Could not get the full path to the model store directory.", e);
            }
            this.svmEngine.saveModel(model, absoluteModelPath);
            return model;
        } catch (SaveSvmModelException
                | FailedParsingException
                | FailedQuestionTokenMapException
                | ReadInputTextException
                | FailedConllMapException e) {
            throw new BuildModelException("Failed to build the question classifier model.", e);
        }
    }

    String applyToHome(final String qasHome) throws ResourceResolverException {
        final String absoluteQasHome = ResourceResolver.getAbsolutePath(qasHome);
        return absoluteQasHome + '\\' + DEFAULT_QAS_MODEL_NAME;
    }
}
