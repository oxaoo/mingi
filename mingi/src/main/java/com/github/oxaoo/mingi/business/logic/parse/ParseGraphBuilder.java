package com.github.oxaoo.mingi.business.logic.parse;

import java.util.Collection;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.04.2017
 */
abstract class ParseGraphBuilder<T> {
    final ParseGraph<T> parseGraph;

    ParseGraphBuilder() {
        this.parseGraph = new ParseGraph<>();
    }

    ParseGraphBuilder(ParseGraph<T> parseGraph) {
        this.parseGraph = parseGraph;
    }

    public ParseGraph<T> getParseGraph() {
        return this.parseGraph;
    }

    public abstract ParseGraph<T> build(Collection<T> values);
}
