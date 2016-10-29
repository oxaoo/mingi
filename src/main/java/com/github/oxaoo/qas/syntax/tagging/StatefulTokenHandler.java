package com.github.oxaoo.qas.syntax.tagging;

import org.annolab.tt4j.TokenHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * The stateful token handler which implement the TokenHandler class.
 */
public class StatefulTokenHandler<T> implements TokenHandler<T> {
    private List<PosTuple<T>> tokens = new LinkedList<>();

    @Override
    public void token(T token, String pos, String lemma) {
        this.tokens.add(new PosTuple<T>(token, pos, lemma));
    }

    public List<PosTuple<T>> peekTokens() {
        return this.tokens;
    }

    public List<PosTuple<T>> getTokens() {
        List<PosTuple<T>> snapshotTokens = this.tokens;
        tokens = new LinkedList<>();
        return snapshotTokens;
    }
}

//TODO: think about make Future job as result.
