package com.github.oxaoo.qas.search;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class SentenceScoreHandler {
    private List<SentenceScore> sentenceScores;

    public SentenceScoreHandler(int size) {
//        this.sentenceScores = Stream.generate(SentenceScore::new).limit(size).collect(Collectors.toList());
        this.sentenceScores = IntStream.range(0, size).mapToObj(SentenceScore::new).collect(Collectors.toList());
    }

    public int inc(int sentenceId) {
        return sentenceScores.get(sentenceId).inc();
    }

    public List<SentenceScore> top(int limit) {
        return this.sentenceScores.stream().sorted().limit(limit).collect(Collectors.toList());
    }
}
