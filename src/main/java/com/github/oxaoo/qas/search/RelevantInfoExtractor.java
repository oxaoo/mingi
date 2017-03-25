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

    public static List<String> extract(List<Result> results) {
        List<String> texts = PageExtractor.extract(results);
        List<String> relevantFragments = new ArrayList<>();
        for (int i = 0; i < results.size() && i < texts.size(); i++) {
            List<ExtractInfo> extractInfo = NaiveMatcher.matching(results.get(i).getSnippet(), texts.get(i));
            String relevantFragment = "";
            LOG.info("#{} Page link: {}", i, results.get(i).getLink());
            LOG.info("-= Relevant Info =-");
            for (ExtractInfo info : extractInfo) {
                LOG.info("Snippet: {} \nRelevant sentences:", info.getSnippet());
                for (String rs : info.getRelevantSentences()) {
                    LOG.info(rs);
                    relevantFragment += "\n" + rs;
                }
            }
            relevantFragments.add(relevantFragment);
            LOG.info("-------------------");
        }
        return relevantFragments;
    }
}
