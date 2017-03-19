package com.github.oxaoo.qas.training;

import com.github.oxaoo.qas.exceptions.FindSvmModelException;
import com.github.oxaoo.qas.exceptions.SaveSvmModelException;
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

    public static svm_model findModel(String modelPath) throws FindSvmModelException {
        try {
            return svm.svm_load_model(modelPath);
        } catch (IOException e) {
            throw new FindSvmModelException("Failed to find SVM model of question classifier.", e);
        }
    }

    public static void saveModel(svm_model model, String modelPath) throws SaveSvmModelException {
        try {
            svm.svm_save_model(modelPath, model);
        } catch (IOException e) {
            throw new SaveSvmModelException("Failed to save SVM model of question classifier.", e);
        }
    }


    public svm_model svmTrain(List<QuestionModel> trainQuestions) {
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

    public List<QuestionDomain> svmEvaluate(svm_model trainModel, List<QuestionModel> testQuestions) {
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
}
