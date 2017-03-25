package com.github.oxaoo.qas.search;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
 */
@Data
//@RequiredArgsConstructor
@AllArgsConstructor
public class ExtractInfo {
//    @NonNull
    private final String snippet;
    private final List<String> relevantSentences;// = new ArrayList<>();

//    public void addRelevantSentence(String sentence) {
//        this.relevantSentences.add(sentence);
//    }
}
