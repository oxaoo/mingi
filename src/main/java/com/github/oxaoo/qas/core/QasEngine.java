package com.github.oxaoo.qas.core;

import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.SearchFacade;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class QasEngine {
    private final QuestionClassifier questionClassifier;
    private final SearchFacade searchFacade;

    public QasEngine() throws LoadQuestionClassifierModelException {
        this.questionClassifier = new QuestionClassifier();
        this.searchFacade = new SearchFacade();
    }

    public String answer(String question) {
        QuestionDomain questionDomain = this.questionClassifier.classify(question);
        List<DataFragment> dataFragments = this.searchFacade.collectInfo(question);
        return this.makeAnswer(question, questionDomain, dataFragments);
    }

    //todo implement
    public String makeAnswer(String question, QuestionDomain questionDomain, List<DataFragment> dataFragments) {
        return "n/a";
    }
}
