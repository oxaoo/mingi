package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;

import java.util.List;

/**
 * The Numeric answer maker present handles domains questions of the following type:
 * CODE,
 * COUNT,
 * DATE,
 * DISTANCE,
 * MONEY,
 * ORDER,
 * OTHER,
 * PERIOD,
 * PERCENT,
 * SPEED,
 * TEMP,
 * SIZE,
 * WEIGHT
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.03.2017
 */
public class NumericAnswerMaker {

    //todo impl
    public static String dateAnswer(List<Conll> questionTokens,
                                    List<DataFragment> dataFragments) {
        return null;
    }
}
