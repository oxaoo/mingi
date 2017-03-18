package com.github.oxaoo.qas.training;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public enum PosType {
//    ADJ (adjective), ADV (adverb), NOUN (noun), PRP (preposition), CARD (cardinal number), CONJ (conjunction),
// DT (determiner), PRO (pronoun), VERB (verb), I (interjection)

    A(1),
    N(2),
    P(3),
    C(4),
    M(5),
    S(6),
    V(7),
    R(8),
    Q(9);

    public static final PosType values[] = values();

    private int label;

    PosType(int label) {
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
