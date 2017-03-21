package com.github.oxaoo.qas.search;

import com.github.oxaoo.qas.utils.PropertyManager;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

public class SearchEngine {
    private final static String GOOGLE_CSE_ID = "google.cse.id";
    private final static String GOOGLE_API_KEY = "google.api.key";

    private final String apiKey;
    private final String cseId;

    public SearchEngine() {
        this.apiKey = PropertyManager.getPrivateProperty(GOOGLE_API_KEY);
        this.cseId = PropertyManager.getPrivateProperty(GOOGLE_CSE_ID);
    }

    public void execute() {
        HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer(){
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        };

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.t-systems.ru", 3128));
        HttpTransport httpTransport = new NetHttpTransport.Builder().setProxy(proxy).build();

        Customsearch customsearch = new Customsearch(httpTransport, new JacksonFactory(), httpRequestInitializer);

        try {
            com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse()
                    .list("гора эльбрус");
            list.setKey(this.apiKey);
            list.setCx(this.cseId);
            Search results = list.execute();
            List<Result> items = results.getItems();

            for(Result result:items)
            {
                System.out.println("Title:"+result.getHtmlTitle());

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
