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
@Deprecated
public class SearchFactory_ {
    private static final Logger LOG = LoggerFactory.getLogger(SearchFactory_.class);

    private final SearchEngine searchEngine;
    //todo use enterprise search also
    private final EnterpriseSearch enterpriseSearch;

    public SearchFactory_() {
        this.searchEngine = new SearchEngine();
        this.enterpriseSearch = new EnterpriseSearch();
    }

    public List<DataFragment> collectInfo(String question) {
        List<Result> results = this.searchEngine.find(question);
        List<DataFragment> relevantDataFragments = RelevantInfoExtractor.extract(results);
        relevantDataFragments.forEach(s -> LOG.debug("### {}", s));
        return relevantDataFragments;
    }
}
