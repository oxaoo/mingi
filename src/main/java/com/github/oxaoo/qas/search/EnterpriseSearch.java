package com.github.oxaoo.qas.search;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 23.04.2017
 */
//todo impl use ElasticSearch or Solr
public class EnterpriseSearch {
    private SearchClient searchClient;

    public EnterpriseSearch() {
        this.searchClient = new SearchClient();
    }

    public List<String> find(String query, URI resourcePath) {
        this.searchClient.init(resourcePath);
        String text = this.searchClient.extract(query);
        return this.relevantOrdering(text);
    }

    private List<String> relevantOrdering(String text) {
        return Collections.emptyList();
    }

    private class SearchClient {
        //todo stub class
        public String extract(String query) {
            return null;
        }

        public void init(URI resourcePath) {

        }
    }
}
