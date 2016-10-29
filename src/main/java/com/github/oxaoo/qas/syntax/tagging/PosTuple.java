package com.github.oxaoo.qas.syntax.tagging;

public class PosTuple<T> {
    T token;
    String pos;
    String lemma;

    public PosTuple(T token, String pos, String lemma) {
        this.token = token;
        this.pos = pos;
        this.lemma = lemma;
    }

    public T getToken() {
        return token;
    }

    public String getPos() {
        return pos;
    }

    public String getLemma() {
        return lemma;
    }

    @Override
    public String toString() {
        return "PosTuple{" +
                "token=" + token +
                ", pos='" + pos + '\'' +
                ", lemma='" + lemma + '\'' +
                '}';
    }
}
