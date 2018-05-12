package com.github.oxaoo.mingi.core.question.training;

import com.github.oxaoo.mingi.core.question.QuestionDomain;
import com.github.oxaoo.mingi.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.google.gson.GsonBuilder;
import libsvm.svm_model;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
@AllArgsConstructor
public class TrainerQuestionClassifier {
    private static final Logger LOG = LoggerFactory.getLogger(TrainerQuestionClassifier.class);

    private static final String DEFAULT_TRAIN_QUESTIONS_FILE_NAME = "train_questions.txt";
    private static final String DEFAULT_TRAIN_QUESTION_DOMAINS_FILE_NAME = "train_question_domains.txt";
    private static final String DEFAULT_TEST_QUESTIONS_FILE_NAME = "test_questions.txt";
    private static final String DEFAULT_TEST_QUESTION_DOMAINS_FILE_NAME = "test_question_domains.txt";

    private final RussianParser parser;
    private final SvmEngine svmEngine;

    private final String trainQuestionsPath;
    private final String trainDomainsQuestionsPath;
    private final String testQuestionsPath;
    private final String testDomainsQuestionsPath;

    public TrainerQuestionClassifier(final String qasHome, final RussianParser parser, final SvmEngine svmEngine) {
        this.parser = parser;
        this.svmEngine = svmEngine;
        final String normalizedHome = qasHome.endsWith("/") ? qasHome : qasHome + "/";
        this.trainQuestionsPath = normalizedHome + DEFAULT_TRAIN_QUESTIONS_FILE_NAME;
        this.trainDomainsQuestionsPath = normalizedHome + DEFAULT_TRAIN_QUESTION_DOMAINS_FILE_NAME;
        this.testQuestionsPath = normalizedHome + DEFAULT_TEST_QUESTIONS_FILE_NAME;
        this.testDomainsQuestionsPath = normalizedHome + DEFAULT_TEST_QUESTION_DOMAINS_FILE_NAME;
    }

    public svm_model build() throws ReadInputTextException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {

        svm_model trainModel = this.train();
        this.test(trainModel);
        return trainModel;
    }

    private svm_model train() throws ReadInputTextException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        List<QuestionModel> questionModels
                = this.prepareQuestionModel(this.trainQuestionsPath, this.trainDomainsQuestionsPath);
        LOG.debug("Questions model:" + new GsonBuilder().setPrettyPrinting().create().toJson(questionModels));

        return this.svmEngine.svmTrain(questionModels);
    }

    private void test(svm_model trainModel) throws ReadInputTextException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        List<QuestionModel> questionModels
                = this.prepareQuestionModel(this.testQuestionsPath, this.testDomainsQuestionsPath);
        LOG.debug("Questions model:" + new GsonBuilder().setPrettyPrinting().create().toJson(questionModels));

        List<QuestionDomain> evaluatedQuestionsDomains = this.svmEngine.svmEvaluate(trainModel, questionModels);
        this.comparison(evaluatedQuestionsDomains, questionModels);
    }

    private void comparison(List<QuestionDomain> evaluatedQDomains, List<QuestionModel> testQuestions) {
        int error = 0;
        List<Integer> errorIds = new ArrayList<>();
        for (int i = 0; i < evaluatedQDomains.size(); i++) {
            if (evaluatedQDomains.get(i).ordinal() != testQuestions.get(i).getDomain().ordinal()) {
                error++;
                errorIds.add(testQuestions.get(i).getModelId());
//                errorIds.add(i);
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

    private List<QuestionModel> prepareQuestionModel(String questionsFilePath, String questionDomainFilePath)
            throws ReadInputTextException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        List<String> questions = TrainerUtils.readQuestions(questionsFilePath);
        //todo delete the filteringQuestion
        List<String> filteredQuestions = QuestionFilter.filteringQuestion(questions);
        List<QuestionToken> questionsTokens = new ArrayList<>();
        for (String question : filteredQuestions) {
            List<Conll> questionConlls = this.parser.parse(question, Conll.class);
            for (Conll conll : questionConlls) {
                questionsTokens.add(QuestionToken.map(conll));
            }
        }
        List<QuestionModel> questionModels = this.buildModels(questionsTokens);
        TrainerUtils.readDomainsMap(questionDomainFilePath, questionModels);
        return questionModels;
    }


    private List<QuestionModel> buildModels(List<QuestionToken> questionTokens) {
        List<QuestionModel> questionModels = new ArrayList<>();
        int prevTokenId = 0;
        int modelId = 0;
        QuestionModel questionModel = new QuestionModel(++modelId);
        for (QuestionToken token : questionTokens) {
            //if current token from next question
            if (token.getTokenId() <= prevTokenId) {
                questionModels.add(questionModel);
                questionModel = new QuestionModel(++modelId);
            }
            questionModel.addQuestionToken(token);
            prevTokenId = token.getTokenId();
        }
        //add last question
        if (!questionTokens.isEmpty()) {
            questionModels.add(questionModel);
        }
        return questionModels;
    }

}
