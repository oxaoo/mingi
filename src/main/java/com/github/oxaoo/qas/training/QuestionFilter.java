package com.github.oxaoo.qas.training;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 03.04.2017
 */
public class QuestionFilter {
    public static List<String> filteringQuestion(List<String> questions) {
        return questions.stream()
                .map(q -> q.replaceAll("(\\[[^]]*\\])?(\\([^)]*\\))?([—«»\"`‚„‘’“”%;:,.!]*[\\[.*\\]]*)?", ""))
                .collect(Collectors.toList());
    }
}
