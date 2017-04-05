package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.search.DataFragment;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * The Location answer maker present handles domains questions of the following type:
 * CITY,
 * COUNTRY,
 * MOUNTAIN,
 * OTHER_NUMERIC,
 * STATE
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 05.04.2017
 */
public class LocationAnswerMaker {
    public static List<Callable<String>> dateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments) {
        return null;
    }
}
