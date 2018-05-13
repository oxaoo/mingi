package com.github.oxaoo.mingi.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 31.03.2017
 */
public class RelativeConllNodeComparator implements GraphComparator<Conll> {
    @Override
    public Conll find(Conll current, Conll relative) {
        if (current.getHead() == relative.getId() || current.getId() == relative.getHead()) return relative;
        else return null;
    }
}
