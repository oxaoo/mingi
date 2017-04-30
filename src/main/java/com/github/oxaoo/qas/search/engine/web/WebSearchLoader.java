package com.github.oxaoo.qas.search.engine.web;

import com.github.oxaoo.qas.search.engine.SearchLoader;
import com.github.oxaoo.qas.search.common.ProxyManager;
import com.google.api.services.customsearch.model.Result;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchLoader implements SearchLoader<List<Result>, List<WebSearchUnit>> {
    private static final Logger LOG = LoggerFactory.getLogger(WebSearchLoader.class);

    @Override
    public List<WebSearchUnit> load(List<Result> results) {
        List<Callable<WebSearchUnit>> fetchingTasks = results.stream()
                .map(r -> (Callable<WebSearchUnit>) () -> this.fetchResult(r))
                .collect(Collectors.toList());
        ExecutorService executor = Executors.newFixedThreadPool(results.size());
        try {
            return executor.invokeAll(fetchingTasks).stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Exception during fetching page. Cause: {}", e.getMessage());
                    return WebSearchUnit.emptyUnit();
                }
            }).filter(u -> !u.isEmpty()).collect(Collectors.toList());
        } catch (InterruptedException e) {
            LOG.error("Exception during invoke concurrent tasks of fething pages. Cause: {}", e.getMessage());
            return Collections.emptyList();
        } finally {
            this.shutdownExecutor(executor);
        }
    }

    private WebSearchUnit fetchResult(Result result) {
        String link = result.getLink();
        try {
            Connection connection = Jsoup.connect(link);
            Proxy proxy = ProxyManager.getProxyIf();
            if (proxy != null) {
                connection.proxy(proxy);
            }
            Document doc = connection.get();
            String text = doc.body().text();
            LOG.debug("\nLink: {}, \nText: {}, \nSnippet: {}\n", link, text, result.getSnippet());
            return new WebSearchUnit(result, text);
        } catch (IOException e) {
            LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
            return WebSearchUnit.emptyUnit();
        }
    }

    private void shutdownExecutor(ExecutorService executor) {
        try {
            LOG.debug("Attempt to shutdown the Executor of Web Search Loader");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.warn("The shutdown task is interrupted. Cause: {}", e.getMessage());
        } finally {
            if (!executor.isTerminated()) {
                LOG.error("To cancel the non-finished tasks");
            }
            executor.shutdownNow();
            LOG.debug("Executor shutdown finished");
        }
    }
}
