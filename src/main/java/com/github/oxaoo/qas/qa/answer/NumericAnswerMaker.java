package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;
import org.jsoup.select.Collector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    //todo move parse up!
    public static List<String> dateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws FailedParsingException {
        RussianParser parser = ParserManager.getParser();
        List<String> answers = new ArrayList<>();
        for (DataFragment dataFragment : dataFragments) {
            for (RelevantInfo relevantInfo : dataFragment.getRelevantInfoList()) {
                for (String sentence : relevantInfo.getRelevantSentences()) {
                    List<Conll> conlls = parser.parse(sentence, Conll.class);
                    ParseGraph<Conll> graph = ParseGraphBuilder.make(conlls);
                    questionTokens = questionTokens.stream()
                            .sorted(Comparator.comparingInt(Conll::getHead))
                            .collect(Collectors.toList());
                    Conll headConll = questionTokens.get(0);
                    ParseNode<Conll> foundNode = graph.find(headConll, new ConllGraphComparator());
                    List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
                    List<Conll> dependentConlls = dependentNodes.stream()
                            .map(ParseNode::getValue)
                            .collect(Collectors.toList());
                    dependentConlls = dependentConlls.stream()
                            .sorted(Comparator.comparingInt(Conll::getId))
                            .collect(Collectors.toList());
                    StringBuilder sb = new StringBuilder();
                    dependentConlls.forEach(c -> sb.append(c.getForm()).append(" "));
                    answers.add(sb.toString());
                }
            }
        }
        return answers;
    }

}
