package com.github.oxaoo.mingi.search.logic;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
@Data
@RequiredArgsConstructor
public class SentenceScore implements Comparable<SentenceScore> {
    @NonNull
    private final int idSentence;
    private int score = 0;

    public int inc() {
        return ++score;
    }

    @Override
    public int compareTo(SentenceScore ss) {
        return Integer.compare(ss.score, this.score);
    }
}
