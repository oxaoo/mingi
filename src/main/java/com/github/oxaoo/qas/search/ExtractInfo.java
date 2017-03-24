package com.github.oxaoo.qas.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractInfo {
    private String snippet;
    private String text;
    private List<String> relevantSentences = new ArrayList<>();
}
