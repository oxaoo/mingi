package com.github.oxaoo.qas.search;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class SearchModel<T, K> {
    private SearchFinder<T> finder;
    private SearchLoader<T, K> loader;
    private SearchRetriever<T, K> retriever;

    public SearchModel(SearchFactory<T, K> searchFactory) {
        this.finder = searchFactory.createSearchFinder();
        this.loader = searchFactory.createSearchLoader();
        this.retriever = searchFactory.createSearchRetriever();
    }

    public List<DataFragment> collectInfo(String question) {
        return this.retriever.retrieve(this.finder, this.loader, question);
    }
}
