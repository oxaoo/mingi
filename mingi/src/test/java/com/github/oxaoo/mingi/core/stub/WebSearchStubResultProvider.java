package com.github.oxaoo.mingi.core.stub;

import com.google.api.services.customsearch.model.Result;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public class WebSearchStubResultProvider {
    private static Queue<List<Result>> stubProvider = new LinkedList<>();

    public static void add(List<Result> results) {
        stubProvider.add(results);
    }

    public static List<Result> provide() {
        List<Result> results = stubProvider.poll();
        if (results == null) return Collections.emptyList();
        return results;
    }

    public static boolean isEmpty() {
        return stubProvider.isEmpty();
    }
}
