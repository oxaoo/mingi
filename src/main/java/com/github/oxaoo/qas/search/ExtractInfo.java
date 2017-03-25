package com.github.oxaoo.qas.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
 */
@Data
@AllArgsConstructor
public class ExtractInfo {
    private final String snippet;
    private final List<String> relevantSentences;
}
