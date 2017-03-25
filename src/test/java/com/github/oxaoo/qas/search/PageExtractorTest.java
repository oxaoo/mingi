package com.github.oxaoo.qas.search;

import com.github.oxaoo.mp4ru.syntax.tokenize.SimpleTokenizer;
import com.google.api.services.customsearch.model.Result;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractorTest {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractorTest.class);

    private PageExtractor pageExtractor;

    @Before
    public void init() {
        pageExtractor = new PageExtractor();
    }

    /**
     * Encoded query test.
     * Catching bug on Jsoup ver. 1.10.2
     */
    @Test
    public void encodedQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        PageExtractor.extract(Collections.singletonList(new Result().setLink(link)));
    }

    @Test
    public void simpleQueryTest() {
        String link = "https://ru.wikipedia.org/wiki/Эльбрус";
        PageExtractor.extract(Collections.singletonList(new Result().setLink(link)));
    }

    @Test
    public void extractRelevantSentencesTest() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        String stubSnippet = "Эльбру́с (карач.-балк. Минги Тау, кабард.-черк. Ошхамахо ) — стратовулкан \n" +
                "на Кавказе ... Эльбрус находится в Боковом хребте Большого Кавказа в 10 \n" +
                "километрах к северу от Главного Кавказского хребта на границе республик ... ";
        Result result = new Result().setLink(link).setSnippet(stubSnippet);
        List<String> relevantFragments = RelevantInfoExtractor.extract(Collections.singletonList(result));
        LOG.info("Relevant fragments:");
        relevantFragments.forEach(s -> LOG.info("{}\n\n", s));
    }









    @Test
    public void extractRelevantSentencesTest_() {
        String link = "https://ru.wikipedia.org/wiki/%D0%AD%D0%BB%D1%8C%D0%B1%D1%80%D1%83%D1%81";
        String stubSnippet = "Эльбру́с (карач.-балк. Минги Тау, кабард.-черк. Ошхамахо ) — стратовулкан \n" +
                "на Кавказе ... Эльбрус находится в Боковом хребте Большого Кавказа в 10 \n" +
                "километрах к северу от Главного Кавказского хребта на границе республик ... ";
        stubSnippet = stubSnippet.replaceAll("[^\\S ]+", "");
        String separateToken = "\\.\\.\\.";
        List<String> snippets = Arrays.stream(stubSnippet.split(separateToken))
                .map(String::trim)
                .map(s -> s.replace(String.valueOf((char) 160), "")) // skip the '&nbsp;'
                .filter(s -> !(s.isEmpty()))
                .collect(Collectors.toList());
        LOG.info("### READY SNIPPETS ###");
        snippets.forEach(LOG::info);
        String text = pageExtractor.extract(Collections.singletonList(new Result().setLink(link))).get(0);

        List<String> sentences = this.sentenceSplitterTest(text);
        LOG.info("### ALL SENTENCES ###");
        sentences.forEach(LOG::info);

        List<String> relevantSentences = new ArrayList<>();
        for (String snippet : snippets) {
            for (String sentence : sentences) {
                if (sentence.contains(snippet)) {
                    relevantSentences.add(sentence);
                    break;
                }
            }
        }

        LOG.info("Relevant sentences:");
        relevantSentences.forEach(LOG::info);

//        List<String> topSentences = sentences.stream().filter(s -> {
//            for (String sp : snippets) {
//                return s.contains(sp);
//            }
//        }).collect(Collectors.toList());
//
//
//        List<String> topSentences = snippets.forEach(sp -> {
//            sentences.stream().filter(s -> s.contains(sp)).collect(Collectors.toList());
//        });
    }

    //    @Test
    public List<String> sentenceSplitterTest(String text) {
        Locale.setDefault(new Locale("ru"));
        List<String> sentences = new ArrayList<>();
//        Locale ru = new Locale("ru", "RU");
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            sentences.add(text.substring(start, end));
//            System.out.println(text.substring(start,end));
        }
        return sentences;
    }

    private List<String> sentenceSplitterTest2(String text) {
        List<String> sentences = new ArrayList<>();

        LOG.debug("List of sentences: ");
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String sentence = text.substring(start, end).replace("\r\n", "");
            if ("null".equals(sentence)) break;
            sentences.add(sentence);
            LOG.debug(sentence);
        }
        return sentences;
    }
}
