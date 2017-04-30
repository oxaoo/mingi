package com.github.oxaoo.qas.search;

import com.github.oxaoo.qas.search.common.ProxyManager;
import com.google.api.services.customsearch.model.Result;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 22.03.2017
 */
@Deprecated
public class PageExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(PageExtractor.class);

    @Deprecated
    public static List<String> extract(List<Result> results) {
        List<String> texts = new ArrayList<>();
        for (Result result : results) {
            String link = result.getLink();
            try {
                Connection connection = Jsoup.connect(link);
                Proxy proxy = ProxyManager.getProxyIf();
                if (proxy != null) {
                    connection.proxy(proxy);
                }
                Document doc = connection.get();

                String text = doc.body().text();
                texts.add(text);
                LOG.debug("\nLink: {}, \nText: {}, \nSnippet: {}\n", link, text, result.getSnippet());
            } catch (IOException e) {
                LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
            }
        }
        return texts;
    }

    public static List<String> concurrentExtract(List<Result> results) {
        List<Callable<String>> extractionTasks = results.stream()
                .map(r -> (Callable<String>) () -> PageExtractor.extractResult(r))
                .collect(Collectors.toList());
        ExecutorService executor = Executors.newFixedThreadPool(results.size());
        try {
            return executor.invokeAll(extractionTasks).stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Exception during execution extract page. Cause: {}", e.getMessage());
                    return "";
                }
            }).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        } catch (InterruptedException e) {
            LOG.error("Exception during invoke concurrent tasks of extraction information. Cause: {}", e.getMessage());
            return Collections.emptyList();
        } finally {
            shutdownExecutor(executor);
        }
    }

    private static String extractResult(Result result) {
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
            return text;
        } catch (IOException e) {
            LOG.error("Failed to get page {}. Cause: {}", link, e.getMessage());
            return "";
        }
    }

    private static void shutdownExecutor(ExecutorService executor) {
        try {
            LOG.debug("Attempt to shutdown the Page Extractor Executor");
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
