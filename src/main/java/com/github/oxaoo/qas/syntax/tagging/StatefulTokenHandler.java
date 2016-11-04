package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TokenHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * The stateful token handler which implement the TokenHandler class.
 *
 * @author Alexander Kuleshov
 * @version 0.1
 * @since 29.10.2016
 */
public class StatefulTokenHandler implements TokenHandler<String> {
    private List<Conll> tokens = new LinkedList<>();
    private int counter = 0;

    @Override
    public void token(String token, String pos, String lemma) {
        this.tokens.add(new Conll(++counter, token, lemma, pos));
        if (this.isTerminateMarks(token)) this.counter = 0;
    }

    public List<Conll> peekTokens() {
        return this.tokens;
    }

    public List<Conll> getTokens() {
        List<Conll> snapshotTokens = this.tokens;
        tokens = new LinkedList<>();
        return snapshotTokens;
    }

    private boolean isTerminateMarks(String token) {
        return token.replaceAll("\\.!?", "").length() == 0;
    }
}

//TODO: think about make Future job as result.
