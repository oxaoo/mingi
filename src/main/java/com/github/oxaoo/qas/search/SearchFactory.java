package com.github.oxaoo.qas.search;

import com.google.api.services.customsearch.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 25.03.2017
 */
public class SearchFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SearchFactory.class);

    private final SearchEngine searchEngine;

    public SearchFactory() {
        this.searchEngine = new SearchEngine();
    }

    public List<DataFragment> collectInfo(String question) {
        List<Result> results = this.searchEngine.find(question);
        List<DataFragment> relevantDataFragments = RelevantInfoExtractor.extract(results);
        relevantDataFragments.forEach(s -> LOG.debug("### {}", s));
        return relevantDataFragments;
    }
}
