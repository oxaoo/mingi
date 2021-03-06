package com.github.oxaoo.mingi.search.engine;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public interface SearchFactory<T, K> {
    SearchFinder<T> createSearchFinder();

    SearchLoader<T, K> createSearchLoader();

    SearchRetriever<T, K> createSearchRetriever();
}
