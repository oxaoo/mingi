package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.syntax.parse.SyntaxAnalyzer;
import com.github.oxaoo.qas.syntax.tagging.PosTagging;
import com.github.oxaoo.qas.syntax.tagging.Conll;
import com.github.oxaoo.qas.syntax.tokenize.SimpleTokenizer;
import org.annolab.tt4j.TreeTaggerException;
import org.maltparser.core.exception.MaltChainedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MaltChainedException, IOException, TreeTaggerException {
        //tokenization.
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        String text = tokenizer.readText();
        List<String> words = tokenizer.tokenization(text);

        //morphological analyze.
        PosTagging pos = new PosTagging();
        List<Conll> tokens = pos.tagging(words);
        pos.writeTokens(tokens);

        //syntactic analyze.
        SyntaxAnalyzer sa = new SyntaxAnalyzer();
        sa.analyze();
    }
}
