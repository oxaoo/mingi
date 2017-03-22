package com.github.oxaoo.qas.search;

import com.google.api.services.customsearch.model.Result;
import org.apache.http.protocol.HTTP;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.HTML;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractorTest {

    private PageExtractor pageExtractor;

    @Before
    public void init() {
        pageExtractor = new PageExtractor();
    }

    /**
     * Encoded query test.
     * Catching bug on Jsoup ver. 1.10.2
     *
     */
    @Test
    public void encodedQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        pageExtractor.extract(Collections.singletonList(new Result().setLink(link)));
    }

    @Test
    public void simpleQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/Эльбрус";
        pageExtractor.extract(Collections.singletonList(new Result().setLink(link)));
    }
}
