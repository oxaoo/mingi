package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.syn.train.SyntaxAnalyzer;
import org.maltparser.core.exception.MaltChainedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws MaltChainedException {
        SyntaxAnalyzer sa = new SyntaxAnalyzer();
        String modelPath = sa.learn();
        LOG.info("Syntax classifier model path: {}", modelPath);
    }
}
