package com.github.oxaoo.qas.search.engine.web;

import com.github.oxaoo.qas.search.engine.SearchFactory;
import com.github.oxaoo.qas.search.engine.SearchFinder;
import com.github.oxaoo.qas.search.engine.SearchLoader;
import com.github.oxaoo.qas.search.engine.SearchRetriever;
import com.google.api.services.customsearch.model.Result;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchEngine implements SearchFactory<List<Result>, List<WebSearchUnit>> {
    @Override
    public SearchFinder<List<Result>> createSearchFinder() {
        return new WebSearchFinder();
    }

    @Override
    public SearchLoader<List<Result>, List<WebSearchUnit>> createSearchLoader() {
        return new WebSearchLoader();
    }

    @Override
    public SearchRetriever<List<Result>, List<WebSearchUnit>> createSearchRetriever() {
        return new WebSearchRetriever();
    }
}
