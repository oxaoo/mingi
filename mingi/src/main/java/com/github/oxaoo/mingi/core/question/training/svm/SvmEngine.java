package com.github.oxaoo.mingi.core.question.training.svm;

import com.github.oxaoo.mingi.core.question.QuestionDomain;
import com.github.oxaoo.mingi.core.question.training.QuestionModel;
import com.github.oxaoo.mingi.core.question.training.QuestionToken;
import com.github.oxaoo.mingi.exceptions.FindSvmModelException;
import com.github.oxaoo.mingi.exceptions.SaveSvmModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 16.03.2017
 */
public class SvmEngine {
    private static final Logger LOG = LoggerFactory.getLogger(SvmEngine.class);

    public SvmModel findModel(final String modelPath) throws FindSvmModelException {
        try {
            final InputStreamReader streamReader = new InputStreamReader(new FileInputStream(modelPath));
            return SvmManager.loadModel(new BufferedReader(streamReader));
        } catch (final IOException e) {
            throw new FindSvmModelException("Failed to find SVM model of question classifier.", e);
        }
    }

    public void saveModel(final SvmModel model, final String modelPath) throws SaveSvmModelException {
        try {
            SvmManager.saveModel(modelPath, model);
        } catch (final IOException e) {
            throw new SaveSvmModelException("Failed to save SVM model of question classifier.", e);
        }
    }


    public SvmModel svmTrain(final List<QuestionModel> trainQuestions) {
        final SvmProblem problem = new SvmProblemWrapper(trainQuestions.size());
        trainQuestions.forEach(question -> {
            problem.initMatrixRow(question.getQuestionTokens().size());
            problem.addVectorValue(question.getDomain().ordinal());
            IntStream.range(0, question.getQuestionTokens().size())
                    .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, question.getQuestionTokens().get(i).getPos().getLabel()))
                    .forEach(pair -> {
                        final SvmNode node = new SvmNodeWrapper(pair.getKey(), pair.getValue());
                        problem.addMatrixValue(node);
                    });
        });

        final SvmParameters parameters = new SvmParametersWrapper();
        parameters.setProbability(1);
        parameters.setGamma(0.5);
        parameters.setNu(0.5);
        parameters.setC(100);
        parameters.setType(SvmType.C_SVC);
        parameters.setKernelType(SvmKernelType.LINEAR);
        parameters.setCacheSize(20000);
        parameters.setEps(0.001);

        return SvmManager.train(problem, parameters);
    }

    public List<QuestionDomain> svmEvaluate(final SvmModel trainModel, final List<QuestionModel> testQuestions) {
        final List<QuestionDomain> questionDomains = new ArrayList<>();
        for (final QuestionModel testQuestion : testQuestions) {
            final QuestionDomain questionDomain = this.svmClassify(trainModel, testQuestion);
            questionDomains.add(questionDomain);
        }
        return questionDomains;
    }

    public QuestionDomain svmClassify(final SvmModel trainModel, final QuestionModel question) {
        final List<QuestionToken> questionTokens = question.getQuestionTokens();
        final List<SvmNode> nodes = IntStream.range(0, questionTokens.size())
                .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, questionTokens.get(i).getPos().getLabel()))
                .map(pair -> new SvmNodeWrapper(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        final int totalClasses = SvmManager.getClassesNumber(trainModel);
        //todo fixme (unused field)
        final int[] labels = SvmManager.getLabels(trainModel, totalClasses);
        final double domainId = SvmManager.predictProbability(trainModel, nodes, totalClasses);
        return this.mapDomain(domainId);
    }

    private QuestionDomain mapDomain(final double idQDomain) {
        final int intDomain = (int) idQDomain;
        return QuestionDomain.values[intDomain];
    }
}
