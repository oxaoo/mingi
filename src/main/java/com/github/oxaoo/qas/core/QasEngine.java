package com.github.oxaoo.qas.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.ParserManager;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.qa.answer.AnswerMaker;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.SearchFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class QasEngine {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngine.class);

    private final QuestionClassifier questionClassifier;
    private final SearchFacade searchFacade;
    private final RussianParser parser;

    public QasEngine() throws InitQasEngineException {
        try {
            this.parser = ParserManager.getParser();
            this.questionClassifier = new QuestionClassifier(this.parser);
        } catch (LoadQuestionClassifierModelException | ProvideParserException e) {
            throw new InitQasEngineException("An error occurred while initializing the QAS Engine.", e);
        }
        this.searchFacade = new SearchFacade();
    }

    //for inject
    public QasEngine(QuestionClassifier questionClassifier, SearchFacade searchFacade, RussianParser parser) {
        this.questionClassifier = questionClassifier;
        this.searchFacade = searchFacade;
        this.parser = parser;
    }

    public Set<String> answer(String question) throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException,
            CreateAnswerException {
        List<Conll> questionTokens = this.parser.parse(question, Conll.class);
        QuestionDomain questionDomain = this.questionClassifier.classify(questionTokens);
        List<DataFragment> dataFragments = this.searchFacade.collectInfo(question);
        return this.makeAnswer(questionTokens, questionDomain, dataFragments);
    }

    private Set<String> makeAnswer(List<Conll> questionTokens,
                                   QuestionDomain questionDomain,
                                   List<DataFragment> dataFragments) throws CreateAnswerException {
        LOG.info("### Stage of make answer ###");
        LOG.info("Question: {}", questionTokens.toString());
        LOG.info("Question domain: {}", questionDomain.name());
        LOG.debug("Data fragments: {}", dataFragments.toString());

        return AnswerMaker.make(questionTokens, questionDomain, dataFragments);
    }
}
