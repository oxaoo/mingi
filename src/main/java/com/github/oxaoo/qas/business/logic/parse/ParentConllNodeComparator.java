package com.github.oxaoo.qas.business.logic.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 31.03.2017
 */
public class ParentConllNodeComparator implements GraphComparator<Conll> {
    @Override
    public Conll find(Conll current, Conll relative) {
        if (current.getHead() == relative.getId()) return relative;
        else return null;
    }
}
