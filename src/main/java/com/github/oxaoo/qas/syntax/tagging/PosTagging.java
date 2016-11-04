package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.github.oxaoo.qas.GlobalPropertyKeys.CONLL_TEXT_FILE;

/**
 * The class represent the Part-of-Speech tagging.
 *
 * @author Alexander Kuleshov
 * @version 2.0
 * @since 29.10.2016
 */
public class PosTagging {
    private static final Logger LOG = LoggerFactory.getLogger(PosTagging.class);

    private final String pathModel = "src/main/resources/TreeTagger/lib/russian-utf8.par";

    static {
        System.setProperty("treetagger.home", "src/main/resources/TreeTagger");
    }

    /**
     * Part-of-Speech tagging the list of tokens.
     *
     * @param tokens the list of tokens
     * @return the list of processed tokens in CoNLL format
     * @throws IOException         throw if classifier's model isn't found
     * @throws TreeTaggerException throw if there are incorrect tokens
     */
    public List<Conll> tagging(List<String> tokens) throws IOException, TreeTaggerException {
        TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<>();
        StatefulTokenHandler tokenHandler = new StatefulTokenHandler();
        try {
            tt.setModel(this.pathModel);
            tt.setHandler(tokenHandler);
            tt.process(tokens);
        } finally {
            tt.destroy();
        }

        return tokenHandler.getTokens();
    }

    public void writeTokens(List<Conll> tokens) {
        File file = new File(CONLL_TEXT_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Conll token : tokens) {
                bw.write(token.toRow());
            }
            bw.close();
        } catch (IOException e) {
            LOG.error("Failed to write the tokens to file [{}]", e.toString());
        }
    }
}
//TODO: read about the locating executables and models: https://reckart.github.io/tt4j/usage.html

