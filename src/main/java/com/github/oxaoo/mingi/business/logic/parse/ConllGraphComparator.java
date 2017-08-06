package com.github.oxaoo.mingi.business.logic.parse;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 30.03.2017
 */
public class ConllGraphComparator implements GraphComparator<Conll> {
    //todo simple impl -> complicate!
    @Override
    public Conll find(Conll wanted, Conll current) {
        if (wanted.getLemma().equals(current.getLemma())
                && wanted.getFeats().equals(current.getFeats())) {
            return current;
        }
        return null;
    }
}
