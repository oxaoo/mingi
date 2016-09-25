package com.github.oxaoo.qas.qa;

import com.github.oxaoo.qas.syn.train.SyntaxAnalyzer;
import org.maltparser.core.exception.MaltChainedException;

public class Main {
    public static void main(String[] args) throws MaltChainedException {
        SyntaxAnalyzer sa = new SyntaxAnalyzer();
        sa.learn();
    }
}
