package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.03.2017
 */
public class AnswerMaker {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerMaker.class);

    //todo impl
    public static String make(List<Conll> questionTokens,
                              QuestionDomain questionDomain,
                              List<DataFragment> dataFragments) {
        switch (questionDomain) {
            case DATE:
                return NumericAnswerMaker.dateAnswer(questionTokens, dataFragments);
            default:
                LOG.error("Incorrect question domain: {}", questionDomain.name());
                return "n/a";
        }
    }
}
