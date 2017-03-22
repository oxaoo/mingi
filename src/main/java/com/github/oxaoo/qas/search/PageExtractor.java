package com.github.oxaoo.qas.search;

import com.google.api.services.customsearch.model.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
public class PageExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractor.class);

    public PageExtractor() {
    }

    public void extract(List<Result> snippets) {
        for (Result snippet : snippets) {
            String link = snippet.getLink();
            try {
                Document doc = Jsoup.connect(link)
//                        .proxy("proxy.t-systems.ru", 3128)
                        .get();

                String text = doc.body().text();
                LOG.info("Link: {}, \nText: {}", link, text);
            } catch (IOException e) {
                LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
