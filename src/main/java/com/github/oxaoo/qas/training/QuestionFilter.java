package com.github.oxaoo.qas.training;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
 */
public class QuestionFilter {

    public static List<String> filteringQuestion(List<String> questions) {
        return questions.stream()
                .map(q -> q.replaceAll("[—«»\"`‚„‘’“”%;:.?!(){}\\[\\]\\p{Z}]+", ""))
                .collect(Collectors.toList());
    }
}
