package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TokenHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * The stateful token handler which implement the TokenHandler class.
 *
 * @author Alexander Kuleshov
 * @version 2.0
 * @since 29.10.2016
 */
public class StatefulTokenHandler implements TokenHandler<String> {
    private List<Conll> tokens = new LinkedList<>();

    @Override
    public void token(String token, String pos, String lemma) {
        this.tokens.add(new Conll(token, lemma, pos));
    }

    public List<Conll> peekTokens() {
        return this.tokens;
    }

    public List<Conll> getTokens() {
        List<Conll> snapshotTokens = this.tokens;
        tokens = new LinkedList<>();
        return snapshotTokens;
    }
}

//TODO: think about make Future job as result.
