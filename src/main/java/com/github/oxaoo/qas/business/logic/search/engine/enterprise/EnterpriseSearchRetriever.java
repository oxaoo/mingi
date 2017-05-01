package com.github.oxaoo.qas.business.logic.search.engine.enterprise;

import com.github.oxaoo.qas.business.logic.search.data.DataFragment;
import com.github.oxaoo.qas.business.logic.search.engine.SearchFinder;
import com.github.oxaoo.qas.business.logic.search.engine.SearchLoader;
import com.github.oxaoo.qas.business.logic.search.engine.SearchRetriever;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
//todo impl use ElasticSearch or Solr
public class EnterpriseSearchRetriever implements SearchRetriever<File, List<String>> {
    private EnterpriseClient enterpriseClient;

    @Override
    public List<DataFragment> retrieve(List<String> textFragments) {
        //todo implement
        this.enterpriseClient = new EnterpriseClient();
        this.enterpriseClient.init(null);
        String extractInfo = this.enterpriseClient.extract(textFragments);
        return Collections.emptyList();
    }

    @Override
    public List<DataFragment> retrieve(SearchFinder<File> searchFinder,
                                       SearchLoader<File, List<String>> searchLoader,
                                       String question) {
        //todo implement
        File fileResult = searchFinder.find();
        List<String> textFragments = searchLoader.load(fileResult);
        return this.retrieve(textFragments);
    }

    private class EnterpriseClient {
        //todo stub class
        public String extract(Collection<?> data) {
            return null;
        }

        public void init(URI resourcePath) {

        }
    }
}
