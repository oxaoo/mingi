package com.github.oxaoo.qas.search;

import com.github.oxaoo.qas.utils.PropertyManager;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;

public class SearchEngine {
    private static final Logger LOG = LoggerFactory.getLogger(SearchEngine.class);

    private final static String GOOGLE_CSE_ID_PROPERTY = "google.cse.id";
    private final static String GOOGLE_API_KEY_PROPERTY = "google.api.key";

    private final String apiKey;
    private final String cseId;

    private Customsearch customsearch;

    public SearchEngine() {
        this.apiKey = PropertyManager.getPrivateProperty(GOOGLE_API_KEY_PROPERTY);
        this.cseId = PropertyManager.getPrivateProperty(GOOGLE_CSE_ID_PROPERTY);
        this.init();
    }

    private void init() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.t-systems.ru", 3128));
        HttpTransport httpTransport = new NetHttpTransport.Builder().setProxy(proxy).build();
        this.customsearch = new Customsearch(httpTransport, new JacksonFactory(), httpRequest -> {
        });
    }

    public void find(String query) {
        List<Result> items;
        try {
            Search results = this.customsearch.cse().list(query)
                    .setKey(this.apiKey)
                    .setCx(this.cseId)
                    .execute();
            items = results.getItems();
        } catch (IOException e) {
            LOG.error("Error during the query to the search engine. Cause: {}", e.getMessage());
            items = Collections.emptyList();
        }

        for (Result result : items) {
            LOG.info("Title: {} \nSnippet: {} \nLink: {} \n\n",
                    result.getTitle(), result.getSnippet(), result.getLink());
        }
    }
}
