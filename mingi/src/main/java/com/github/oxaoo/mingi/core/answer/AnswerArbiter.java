package com.github.oxaoo.mingi.core.answer;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.1
 * @since 31.08.2017
 */
public class AnswerArbiter {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerArbiter.class);

    public Set<String> rank(List<String> answers) {
        Map<String, Integer> answerScores = new HashMap<>();
        for (String answer : answers) {
            List<ExtractedResult> list = FuzzySearch.extractAll(answer, answers);
            int score = list.stream().mapToInt(ExtractedResult::getScore).sum();
            answerScores.put(answer, score);
        }
        LinkedHashMap<String, Integer> rankedAnswers = answerScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        LOG.info("Ranked answers: ");
        for (String answer : rankedAnswers.keySet()) {
            LOG.info("Answer: {}, score: {}", answer, rankedAnswers.get(answer));
        }
        return rankedAnswers.keySet();
    }
}
