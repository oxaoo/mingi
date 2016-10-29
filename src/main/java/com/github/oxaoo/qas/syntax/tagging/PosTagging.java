package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import java.io.IOException;
import java.util.List;

/**
 * The class represent the Part-of-Speech tagging.
 */
public class PosTagging {
    private final String pathModel = "src/main/resources/TreeTagger/lib/russian-utf8.par";

    static {
        System.setProperty("treetagger.home", "src/main/resources/TreeTagger");
    }

    public List<PosTuple<String>> tagging(List<String> tokens) throws IOException, TreeTaggerException {
        TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<>();
        StatefulTokenHandler<String> tokenHandler = new StatefulTokenHandler<>();
        try {
            tt.setModel(this.pathModel);
            tt.setHandler(tokenHandler);
            tt.process(tokens);
        }
        finally {
            tt.destroy();
        }

        return tokenHandler.getTokens();
    }
}
//TODO: read about the locating executables and models: https://reckart.github.io/tt4j/usage.html

