package com.github.oxaoo.qas.search.engine.enterprise;

import com.github.oxaoo.qas.search.engine.SearchFactory;
import com.github.oxaoo.qas.search.engine.SearchFinder;
import com.github.oxaoo.qas.search.engine.SearchLoader;
import com.github.oxaoo.qas.search.engine.SearchRetriever;

import java.io.File;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
//todo impl use ElasticSearch or Solr
public class EnterpriseSearchEngine implements SearchFactory<File, List<String>> {
    @Override
    public SearchFinder<File> createSearchFinder() {
        return new EnterpriseSearchFinder();
    }

    @Override
    public SearchLoader<File, List<String>> createSearchLoader() {
        return new EnterpriseSearchLoader();
    }

    @Override
    public SearchRetriever<File, List<String>> createSearchRetriever() {
        return new EnterpriseSearchRetriever();
    }
}
