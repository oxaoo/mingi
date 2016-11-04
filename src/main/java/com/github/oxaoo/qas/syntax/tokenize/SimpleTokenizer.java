package com.github.oxaoo.qas.syntax.tokenize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * The simple tokenizer, breaking the text to the words.
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
        try(BufferedReader br = new BufferedReader(new FileReader(TEXT_FILE))) {
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
     * To split the text on words.
     *
     * @param text a input text
     * @return list of words
     */
    public List<String> tokenization(String text) {
        LOG.debug("List of words:");
        return Arrays.asList(text.split("[—«»\"`‚„‘’“”%,;:.!?\\s]+"));
    }
}

//TODO: view about tokenization: https://reckart.github.io/tt4j/tokenizer.html
