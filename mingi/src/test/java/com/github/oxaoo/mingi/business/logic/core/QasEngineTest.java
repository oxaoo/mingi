package com.github.oxaoo.mingi.business.logic.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.mingi.business.logic.qa.answer.AnswerEngine;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionClassifier;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionDomain;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 26.03.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class QasEngineTest {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngineTest.class);

    @InjectMocks
    private QasEngine engine;
    @Mock
    private QuestionClassifier classifier;
    @Mock
    private SearchEngine searchEngine;
    @Mock
    private AnswerEngine answerEngine;
    @Mock
    private static RussianParser parser;


    @Test
    @SuppressWarnings({"unchecked", "PMD.JUnitTestsShouldIncludeAssert"})
    public void answerTest() throws FailedParsingException,
            FailedConllMapException, FailedQuestionTokenMapException {
        String question = "the question";
        QuestionDomain domain = QuestionDomain.DATE;
        Mockito.when(this.classifier.classify(Mockito.anyList())).thenReturn(domain);

        this.engine.answer(question, this.searchEngine);

        Mockito.verify(parser).parse(question, Conll.class);
        Mockito.verify(this.classifier).classify(Mockito.anyList());
        Mockito.verify(this.searchEngine).collectInfo(question);
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void shutdownTest() throws InitQasEngineException {
        this.engine.shutdown();
        Mockito.verify(this.answerEngine).shutdownExecutor();
    }
}
