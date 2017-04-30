package com.github.oxaoo.qas.search.web;

import com.google.api.services.customsearch.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
@Data
@AllArgsConstructor
public class WebSearchUnit {
    private final Result result;
    private final String text;

    boolean isEmpty() {
        return result == null && text == null;
    }

    static WebSearchUnit emptyUnit() {
        return new WebSearchUnit(null, null);
    }
}
