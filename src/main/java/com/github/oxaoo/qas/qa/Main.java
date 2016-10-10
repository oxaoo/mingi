package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.syntax.parse.SyntaxAnalyzer;
import org.maltparser.core.exception.MaltChainedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    //is necessary to use the vm option -Xmx6g
    public static void main(String[] args) throws MaltChainedException {
        final SyntaxAnalyzer syntax = new SyntaxAnalyzer();
        final boolean resultSyntax = syntax.analyze();
        LOG.info("Result of syntax analyze: {}", resultSyntax);
    }
}
