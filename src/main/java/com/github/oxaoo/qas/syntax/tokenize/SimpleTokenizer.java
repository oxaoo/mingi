package com.github.oxaoo.qas.syntax.tokenize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The simple tokenizer, breaking the text to the words.
 *
 * @author Alexander Kuleshov
 * @version 0.1
 * @since 17.10.2016
 */
public class SimpleTokenizer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleTokenizer.class);
    private final static String TEXT_FILE = "src/main/resources/input/text.txt";

    /**
     * Read text from file.
     *
     * @return the text
     */
    public String readText() {
        try (BufferedReader br = new BufferedReader(new FileReader(TEXT_FILE))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error("Failed to read text: " + e.toString());
        }
        return null;
    }

    /**
     * To split the text on words using regular expressions.
     *
     * @param text the input text
     * @return list of words
     */
    @Deprecated
    public List<String> tokenizationRegular(String text) {
        LOG.debug("List of words:");
        return Arrays.asList(text.split("[—«»\"`‚„‘’“”%,;:.!?\\s]+"));
    }

    /**
     * To split the text on words.
     *
     * @param text the input text
     * @return list of words
     */
    public List<String> tokenization(String text) {
        List<String> tokens = new ArrayList<>();
        Locale ruLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
        BreakIterator bi = BreakIterator.getWordInstance(ruLocale);

        text = this.trimming(text);
        bi.setText(text);
        int cur = bi.first();
        int prev = 0;
        while (cur != BreakIterator.DONE) {
            String token = text.substring(prev, cur);
            if (token.replaceAll("[—«»\"`‚„‘’“”%;:\\s]+", "").length() > 0)
                tokens.add(token);
            prev = cur;
            cur = bi.next();
        }
        return tokens;
    }

    /**
     * Deleting characters unrecognizable the BreakIterator.
     *
     * @param text the input text
     * @return the trimmed text
     */
    private String trimming(String text) {
        return text.replace("...", "");
    }
}
