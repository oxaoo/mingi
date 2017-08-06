package com.github.oxaoo.mingi.business.logic.core;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mingi.business.logic.core.stub.WebSearchEngineStub;
import com.github.oxaoo.mingi.business.logic.core.stub.WebSearchStubResultProvider;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchUnit;
import com.google.api.services.customsearch.model.Result;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public class QasEngineITest {
    private static final Logger LOG = LoggerFactory.getLogger(QasEngineTest.class);

    @Test
    public void initTest() throws InitQasEngineException {
        QasEngine engine = new QasEngine();
        Assert.assertNotNull(engine.getParser());
        Assert.assertNotNull(engine.getQuestionClassifier());
        Assert.assertNotNull(engine.getAnswerEngine());
    }

    /**
     * Answering using the web search engine stub.
     *
     * @throws InitQasEngineException          the init qas engine exception
     * @throws FailedParsingException          the failed parsing exception
     * @throws FailedConllMapException         the failed conll map exception
     * @throws FailedQuestionTokenMapException the failed question token map exception
     */
    @Test
    public void answerWithFinderStubTest() throws InitQasEngineException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        SearchEngine<List<Result>, List<WebSearchUnit>> searchEngine = new SearchEngine<>(new WebSearchEngineStub());
        QasEngine qasEngine = new QasEngine();
        String question = "В каком году затонул Титаник?";

        Assert.assertTrue(WebSearchStubResultProvider.isEmpty());
        this.provideResult();
        Assert.assertFalse(WebSearchStubResultProvider.isEmpty());

        Set<String> answers = qasEngine.answer(question, searchEngine);
        answers.forEach(LOG::info);
        Assert.assertFalse(answers.isEmpty());
    }

    private void provideResult() {
        String link = "https://ru.wikipedia.org/wiki/%D0%A2%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%BA";
        String snippet = "«Тита́ник» (англ. Titanic) — британский трансатлантический пароход, второй лайнер класса «Олимпик»."
                + " Строился в Белфасте на верфи «Харленд энд Вулф» с 1909 по 1912 год\n"
                + "В 2:20 15 апреля, разломившись на две части, «Титаник» затонул, унеся жизни 1496 человек."
                + " 712 спасшихся человек";

        Result result = new Result()
                .setLink(link)
                .setSnippet(snippet);
        List<Result> results = Collections.singletonList(result);
        WebSearchStubResultProvider.add(results);
    }

    /**
     * Answering using the pure web search engine.
     *
     * @throws InitQasEngineException          the init qas engine exception
     * @throws FailedParsingException          the failed parsing exception
     * @throws FailedConllMapException         the failed conll map exception
     * @throws FailedQuestionTokenMapException the failed question token map exception
     */
    @Test
    public void answerWithWebSearchEngineTest() throws InitQasEngineException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        QasEngine qasEngine = new QasEngine();
        String question = "Что такое титан?";
        SearchEngine<?, ?> webSearchEngine = new SearchEngine<>(new WebSearchEngine());
        Set<String> answers = qasEngine.answer(question, webSearchEngine);
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
        qasEngine.shutdown();
        Assert.assertFalse(answers.isEmpty());
    }
}
