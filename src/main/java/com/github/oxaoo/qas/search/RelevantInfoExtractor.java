package com.github.oxaoo.qas.search;

import com.google.api.services.customsearch.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class RelevantInfoExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(RelevantInfoExtractor.class);

    public static List<DataFragment> extract(List<Result> results) {
        List<String> texts = PageExtractor.concurrentExtract(results);
        List<DataFragment> dataFragments = new ArrayList<>();
        for (int i = 0; i < results.size() && i < texts.size(); i++) {
            List<RelevantInfo> relevantInfo = NaiveMatcher.matching(results.get(i).getSnippet(), texts.get(i));
            dataFragments.add(new DataFragment(results.get(i).getLink(), relevantInfo));
        }
        return dataFragments;
    }
}
