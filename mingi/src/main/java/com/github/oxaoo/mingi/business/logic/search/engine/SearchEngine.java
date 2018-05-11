package com.github.oxaoo.mingi.business.logic.search.engine;

import com.github.oxaoo.mingi.business.logic.search.data.DataFragment;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class SearchEngine<T, K> {
    private SearchFinder<T> finder;
    private SearchLoader<T, K> loader;
    private SearchRetriever<T, K> retriever;

    public SearchEngine(SearchFactory<T, K> searchFactory) {
        this.finder = searchFactory.createSearchFinder();
        this.loader = searchFactory.createSearchLoader();
        this.retriever = searchFactory.createSearchRetriever();
    }

    public List<DataFragment> collectInfo(String question) {
        return this.retriever.retrieve(this.finder, this.loader, question);
    }
}
