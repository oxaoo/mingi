package com.github.oxaoo.qas.business.logic.search.engine.web;

import com.github.oxaoo.qas.business.logic.search.engine.SearchFinder;
import com.github.oxaoo.qas.business.logic.search.common.ProxyManager;
import com.github.oxaoo.qas.business.logic.common.PropertyManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchFinder implements SearchFinder<List<Result>> {
    private static final Logger LOG = LoggerFactory.getLogger(WebSearchFinder.class);

    private final static String GOOGLE_CSE_ID_PROPERTY = "google.cse.id";
    private final static String GOOGLE_API_KEY_PROPERTY = "google.api.key";

    private final String apiKey;
    private final String cseId;

    private Customsearch customsearch;

    public WebSearchFinder() {
        this.apiKey = PropertyManager.getPrivateProperty(GOOGLE_API_KEY_PROPERTY);
        this.cseId = PropertyManager.getPrivateProperty(GOOGLE_CSE_ID_PROPERTY);
        this.init();
    }

    private void init() {
        NetHttpTransport.Builder netBuilder = new NetHttpTransport.Builder();
        Proxy proxy = ProxyManager.getProxyIf();
        if (proxy != null) {
            netBuilder.setProxy(proxy);
        }
        this.customsearch = new Customsearch(netBuilder.build(), new JacksonFactory(), httpRequest -> {
        });
    }


    @Override
    public List<Result> find() {
        throw new UnsupportedOperationException("Method is not supported by the Web Search Finder.");
    }

    @Override
    public List<Result> find(String query) {
        List<Result> items;
        try {
            Search results = this.customsearch.cse().list(query)
                    .setKey(this.apiKey)
                    .setCx(this.cseId)
                    .execute();
            items = results.getItems();
        } catch (IOException e) {
            LOG.error("Error during the query to the web search engine. Cause: {}", e.getMessage());
            items = Collections.emptyList();
        }

        for (Result result : items) {
            LOG.debug("Title: {} \nSnippet: {} \nLink: {} \n\n",
                    result.getTitle(), result.getSnippet(), result.getLink());
        }
        return items;
    }
}
