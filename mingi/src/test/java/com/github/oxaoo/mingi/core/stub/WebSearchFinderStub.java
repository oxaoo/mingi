package com.github.oxaoo.mingi.core.stub;

import com.github.oxaoo.mingi.search.engine.web.WebSearchFinder;
import com.google.api.services.customsearch.model.Result;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchFinderStub extends WebSearchFinder {
    @Override
    public List<Result> find(String query) {
        return WebSearchStubResultProvider.provide();
    }
}
