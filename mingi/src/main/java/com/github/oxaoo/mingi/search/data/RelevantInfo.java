package com.github.oxaoo.mingi.search.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 24.03.2017
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelevantInfo {
    private String snippet;
    private List<String> relevantSentences;
}
