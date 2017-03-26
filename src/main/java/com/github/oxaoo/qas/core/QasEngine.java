package com.github.oxaoo.qas.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.SearchFacade;
import com.github.oxaoo.qas.training.TrainerQuestionClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class QasEngine {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngine.class);

    private final QuestionClassifier questionClassifier;
    private final SearchFacade searchFacade;

    public QasEngine() throws LoadQuestionClassifierModelException {
        this.questionClassifier = new QuestionClassifier();
        this.searchFacade = new SearchFacade();
    }

    public String answer(String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        QuestionDomain questionDomain = this.questionClassifier.classify(question);
        List<DataFragment> dataFragments = this.searchFacade.collectInfo(question);
        return this.makeAnswer(question, questionDomain, dataFragments);
    }

    //todo implement
    public String makeAnswer(String question, QuestionDomain questionDomain, List<DataFragment> dataFragments) {
        LOG.info("### Stage of make answer ###");
        LOG.info("Question: {}", question);
        LOG.info("Question domain: {}", questionDomain.name());
        LOG.info("Data fragments: {}", dataFragments.toString());

        return "n/a";
    }
}
