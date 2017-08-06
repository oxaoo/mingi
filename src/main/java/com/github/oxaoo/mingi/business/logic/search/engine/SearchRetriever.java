package com.github.oxaoo.mingi.business.logic.search.engine;

import com.github.oxaoo.mingi.business.logic.search.data.DataFragment;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public interface SearchRetriever<T, K> {
    List<DataFragment> retrieve(K k);

    List<DataFragment> retrieve(SearchFinder<T> searchFinder, SearchLoader<T, K> searchLoader, String question);
}
