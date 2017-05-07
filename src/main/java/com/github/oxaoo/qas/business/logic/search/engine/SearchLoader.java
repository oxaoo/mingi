package com.github.oxaoo.qas.business.logic.search.engine;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.04.2017
 */
public interface SearchLoader<T, K> {
    K load(T t);
}