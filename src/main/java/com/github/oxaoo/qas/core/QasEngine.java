package com.github.oxaoo.qas.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.ParserManager;
import com.github.oxaoo.qas.qa.answer.AnswerEngine;
import com.github.oxaoo.qas.qa.question.QuestionClassifier;
import com.github.oxaoo.qas.qa.question.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.SearchFactory;
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
    private final SearchFactory searchFactory;
    private final RussianParser parser;

    private final AnswerEngine answerEngine;

    public QasEngine() throws InitQasEngineException {
        try {
            this.parser = ParserManager.getParser();
            this.questionClassifier = new QuestionClassifier(this.parser);
        } catch (LoadQuestionClassifierModelException | ProvideParserException e) {
            throw new InitQasEngineException("An error occurred while initializing the QAS Engine.", e);
        }
        this.searchFactory = new SearchFactory();
        this.answerEngine = new AnswerEngine(this.parser);
    }

    //for inject
    public QasEngine(QuestionClassifier questionClassifier, SearchFactory searchFactory, RussianParser parser, AnswerEngine answerEngine) {
        this.questionClassifier = questionClassifier;
        this.searchFactory = searchFactory;
        this.parser = parser;
        this.answerEngine = answerEngine;
    }

    public Set<String> answer(String question) throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        List<Conll> questionTokens = this.parser.parse(question, Conll.class);
        QuestionDomain questionDomain = this.questionClassifier.classify(questionTokens);
        List<DataFragment> dataFragments = this.searchFactory.collectInfo(question);
        return this.makeAnswer(questionTokens, questionDomain, dataFragments);
    }

    private Set<String> makeAnswer(List<Conll> questionTokens,
                                   QuestionDomain questionDomain,
                                   List<DataFragment> dataFragments) {
        LOG.info("### Stage of make answer ###");
        LOG.info("Question: {}", questionTokens.toString());
        LOG.info("Question domain: {}", questionDomain.name());
        LOG.debug("Data fragments: {}", dataFragments.toString());

        return this.answerEngine.make(questionTokens, questionDomain, dataFragments);
    }

    public void shutdown() {
        this.answerEngine.shutdownExecutor();
    }
}
