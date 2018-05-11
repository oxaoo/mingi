package com.github.oxaoo.mingi.business.logic.search;

import com.github.oxaoo.mingi.business.logic.search.engine.SearchLoader;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchLoader;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchUnit;
import com.google.api.services.customsearch.model.Result;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractorTest {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractorTest.class);

    private SearchLoader<List<Result>, List<WebSearchUnit>> searchLoader;

    @Before
    public void init() {
        this.searchLoader = new WebSearchLoader();
    }

    /**
     * Encoded query test.
     * Catching bug on Jsoup ver. 1.10.2
     */
    @Test
    public void encodedQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        this.searchLoader.load(Collections.singletonList(new Result().setLink(link)));
    }

    @Test
    public void simpleQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/Эльбрус";
        this.searchLoader.load(Collections.singletonList(new Result().setLink(link)));
    }

    @Test
    public void extractRelevantSentencesTest() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        String stubSnippet = "Эльбру́с (карач.-балк. Минги Тау, кабард.-черк. Ошхамахо ) — стратовулкан \n" +
                "на Кавказе ... Эльбрус находится в Боковом хребте Большого Кавказа в 10 \n" +
                "километрах к северу от Главного Кавказского хребта на границе республик ... ";
        Result result = new Result().setLink(link).setSnippet(stubSnippet);
        List<WebSearchUnit> units = this.searchLoader.load(Collections.singletonList(result));
        LOG.info("Relevant fragments:");
        units.forEach(s -> LOG.info("{}\n\n", s));
    }
}
