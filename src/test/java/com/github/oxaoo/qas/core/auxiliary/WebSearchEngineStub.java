package com.github.oxaoo.qas.core.auxiliary;

import com.github.oxaoo.qas.search.engine.SearchFinder;
import com.github.oxaoo.qas.search.engine.web.WebSearchEngine;
import com.google.api.services.customsearch.model.Result;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchEngineStub extends WebSearchEngine {
    @Override
    public SearchFinder<List<Result>> createSearchFinder() {
        return new  WebSearchFinderStub();
    }
}
