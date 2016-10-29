package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.syntax.parse.SyntaxAnalyzer;
import com.github.oxaoo.qas.syntax.tagging.PosTagging;
import com.github.oxaoo.qas.syntax.utils.SyntaxUtils;
import org.annolab.tt4j.TreeTaggerException;
import org.maltparser.core.exception.MaltChainedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws MaltChainedException, IOException, TreeTaggerException {
//        final SyntaxAnalyzer syntax = new SyntaxAnalyzer();
//        final boolean resultSyntax = syntax.analyze();
//        LOG.info("Result of syntax analyze: {}", resultSyntax);
//        simpleTextConverter();
        PosTagging pos = new PosTagging();
        pos.tagging();
    }

    public static void simpleTextConverter() {
        SyntaxUtils syntaxUtils = new SyntaxUtils();
        String text = syntaxUtils.readText();
        List<String> words = syntaxUtils.tokenization(text);
        for (String word : words) LOG.info(word);
    }
}
