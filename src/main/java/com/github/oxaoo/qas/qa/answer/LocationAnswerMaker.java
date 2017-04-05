package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.ParserManager;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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

    public static List<Callable<String>> stateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws CreateAnswerException {
        RussianParser parser;
        try {
            parser = ParserManager.getParser();
        } catch (ProvideParserException e) {
            throw new CreateAnswerException("Could not create an answer for a question of type DATE.", e);
        }
        questionTokens = questionTokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        Conll headQuestionToken = questionTokens.get(0);

        List<String> sentences = dataFragments.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());
        return null;
    }
}
