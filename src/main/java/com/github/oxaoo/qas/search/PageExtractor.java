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

    public PageExtractor() {
    }

    public List<String> extract(List<Result> results) {
        List<String> texts = new ArrayList<>();
        List<ExtractInfo> extractInfoList = new ArrayList<>();
        for (Result result : results) {
            String link = result.getLink();
            try {
                Document doc = Jsoup.connect(link)
                        .proxy("proxy.t-systems.ru", 3128)
                        .get();

                String text = doc.body().text();
                texts.add(text);
                extractInfoList.addAll(this.naiveMatching(result.getSnippet(), text));
                LOG.debug("\nLink: {}, \nText: {}, \nSnippet: {}\n", link, text, result.getSnippet());
            } catch (IOException e) {
                LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
                e.printStackTrace();
            }
        }
        return texts;
    }

    public List<ExtractInfo> naiveMatching(String snippetsFragment, String text) {
//        List<String> snippets = results.stream().map(Result::getSnippet).collect(Collectors.toList());

        List<ExtractInfo> extractInfoList = new ArrayList<>();
        List<String> snippets = this.snippetSplitter(snippetsFragment);
        List<String> sentences = this.textSplitter(text);
        Tokenizer tokenizer = new SimpleTokenizer();
        for (String snippet : snippets) {
            List<String> snippetTokens = tokenizer.tokenization(snippet);
            //todo iterate on sentences and count matches, after mostly relevant sentences add to extractInfoList
        }
    }

    private List<String> snippetSplitter(String snippetsFragment) {
        snippetsFragment = snippetsFragment.replaceAll("[^\\S ]+", "");
        String separateToken = "\\.\\.\\.";
        return Arrays.stream(snippetsFragment.split(separateToken))
                .map(String::trim)
                .map(s -> s.replace(String.valueOf((char) 160), "")) // skip the '&nbsp;'
                .filter(s -> !(s.isEmpty()))
                .collect(Collectors.toList());
    }

    private List<String> textSplitter(String text) {
//        Locale.setDefault(new Locale("ru"));
        List<String> sentences = new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance();
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            sentences.add(text.substring(start, end));
        }
        return sentences;
    }
}
