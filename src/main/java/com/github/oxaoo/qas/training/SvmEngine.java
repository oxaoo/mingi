package com.github.oxaoo.qas.training;

import com.github.oxaoo.qas.qa.QuestionDomain;
import libsvm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 16.03.2017
 */
public class SvmEngine {
    private static final Logger LOG = LoggerFactory.getLogger(SvmEngine.class);

    public void run(List<QuestionModel> trainQuestions, List<QuestionModel> testQuestions) {
        svm_model trainModel = this.svmTrain(trainQuestions);
        List<QuestionDomain> evaluatedQDomains = this.svmEvaluate(trainModel, testQuestions);
        this.comparison(evaluatedQDomains, testQuestions);
        try {
            svm.svm_save_model("src/main/resources/qmod.model", trainModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private svm_model svmTrain(List<QuestionModel> trainQuestions) {
        int recordedCount = trainQuestions.size();

        svm_problem problem = new svm_problem();
        problem.x = new svm_node[recordedCount][];
        problem.y = new double[recordedCount];
        problem.l = recordedCount;

        for (int i = 0; i < recordedCount; i++) {
            QuestionModel trainQuestion = trainQuestions.get(i);
            List<QuestionToken> trainQQuestionToken = trainQuestion.getQuestionTokens();
            problem.x[i] = new svm_node[trainQQuestionToken.size()];
            problem.y[i] = trainQuestion.getDomain().ordinal();
            for (int j = 0; j < trainQQuestionToken.size(); j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = trainQQuestionToken.get(j).getPos().getLabel();
                problem.x[i][j] = node;
            }
        }

        svm_parameter parameters = new svm_parameter();
        parameters.probability = 1;
        parameters.gamma = 0.5;
        parameters.nu = 0.5;
        parameters.C = 100;
        parameters.svm_type = svm_parameter.C_SVC;
        parameters.kernel_type = svm_parameter.LINEAR;
        parameters.cache_size = 20000;
        parameters.eps = 0.001;

        return svm.svm_train(problem, parameters);
    }

    private List<QuestionDomain> svmEvaluate(svm_model trainModel, List<QuestionModel> testQuestions) {
        double[] idQDomains = new double[testQuestions.size()];

        for (int i = 0; i < testQuestions.size(); i++) {
            List<QuestionToken> questionToken = testQuestions.get(i).getQuestionTokens();
            svm_node[] nodes = new svm_node[questionToken.size()];
            for (int j = 0; j < questionToken.size(); j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = questionToken.get(j).getPos().getLabel();
                nodes[j] = node;
            }

            int totalClasses = svm.svm_get_nr_class(trainModel);
            int[] labels = new int[totalClasses];
            svm.svm_get_labels(trainModel, labels);

            double[] prob_estimates = new double[totalClasses];
            idQDomains[i] = svm.svm_predict_probability(trainModel, nodes, prob_estimates);
        }
        return this.mapEvaluateDomains(idQDomains);
    }

    private List<QuestionDomain> mapEvaluateDomains(double[] idQDomains) {
        List<QuestionDomain> questionDomains = new ArrayList<>(idQDomains.length);
        for (double id : idQDomains) {
            int intDomain = (int) id;
            questionDomains.add(QuestionDomain.values[intDomain]);
        }
        return questionDomains;
    }

    private void comparison(List<QuestionDomain> evaluatedQDomains, List<QuestionModel> testQuestions) {
        int error = 0;
        List<Integer> errorIds = new ArrayList<>();
        for (int i = 0; i < evaluatedQDomains.size(); i++) {
            if (evaluatedQDomains.get(i).ordinal() != testQuestions.get(i).getDomain().ordinal()) {
                error++;
//                errorIds.add(testQuestions.get(i).getModelId());
                errorIds.add(i);
            }
            LOG.info("Actual - {}:{}, Evaluate - {}:{}",
                    evaluatedQDomains.get(i).name(), evaluatedQDomains.get(i).ordinal(),
                    testQuestions.get(i).getDomain().name(), testQuestions.get(i).getDomain().ordinal());
        }
        LOG.info("Size of test sample: {}, Number of errors: {}", testQuestions.size(), error);
        if (error != 0) {
            LOG.info("List of unrecognized questions correctly:");
            for (int eid : errorIds) LOG.info("# " + eid);
        }
    }


    @Deprecated
    public void run(double[][] xtrain, double[][] xtest, double[][] ytrain, double[][] ytest) {
        svm_model m = svmTrain(xtrain, ytrain);
        double[] ypred = svmPredict(xtest, m);

        for (int i = 0; i < xtest.length; i++) {
            LOG.info("(Actual:" + ytest[i][0] + " Prediction:" + ypred[i] + ")");
        }
    }

    @Deprecated
    private svm_model svmTrain(double[][] xtrain, double[][] ytrain) {
        svm_problem prob = new svm_problem();
        int recordCount = xtrain.length;
        prob.y = new double[recordCount];
        prob.l = recordCount;
        prob.x = new svm_node[recordCount][];

        for (int i = 0; i < recordCount; i++) {
            double[] values = xtrain[i];
            prob.x[i] = new svm_node[values.length];
            for (int j = 0; j < values.length; j++) {
                svm_node node = new svm_node();
                node.index = j;
                node.value = values[j];
                prob.x[i][j] = node;
            }
            prob.y[i] = ytrain[i][0];
        }

        svm_parameter param = new svm_parameter();
        param.probability = 1;
        param.gamma = 0.5;
        param.nu = 0.5;
        param.C = 100;
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        param.cache_size = 20000;
        param.eps = 0.001;

        return svm.svm_train(prob, param);
    }

    @Deprecated
    private double[] svmPredict(double[][] xtest, svm_model model) {
        double[] yPred = new double[xtest.length];

        for (int k = 0; k < xtest.length; k++) {
            double[] fVector = xtest[k];

            svm_node[] nodes = new svm_node[fVector.length];
            for (int i = 0; i < fVector.length; i++) {
                svm_node node = new svm_node();
                node.index = i;
                node.value = fVector[i];
                nodes[i] = node;
            }

            int totalClasses = 2;
            int[] labels = new int[totalClasses];
            svm.svm_get_labels(model, labels);

            double[] prob_estimates = new double[totalClasses];
            yPred[k] = svm.svm_predict_probability(model, nodes, prob_estimates);
        }
        return yPred;
    }
}
