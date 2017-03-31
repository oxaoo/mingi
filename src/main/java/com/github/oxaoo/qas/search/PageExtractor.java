package com.github.oxaoo.qas.search;

import com.github.oxaoo.mp4ru.syntax.tokenize.SimpleTokenizer;
import com.github.oxaoo.mp4ru.syntax.tokenize.Tokenizer;
import com.google.api.services.customsearch.model.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractor.class);

    public static List<String> extract(List<Result> results) {
        List<String> texts = new ArrayList<>();
        for (Result result : results) {
            String link = result.getLink();
            try {
                Document doc = Jsoup.connect(link)
//                        .proxy("proxy.t-systems.ru", 3128)
                        .get();

                String text = doc.body().text();
                texts.add(text);
                LOG.debug("\nLink: {}, \nText: {}, \nSnippet: {}\n", link, text, result.getSnippet());
            } catch (IOException e) {
                LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
            }
        }
        return texts;
    }
}
