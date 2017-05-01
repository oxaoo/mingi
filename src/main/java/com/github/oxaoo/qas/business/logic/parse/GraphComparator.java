package com.github.oxaoo.qas.business.logic.parse;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
public interface GraphComparator<T> {
    T find(T wanted, T current);
}
