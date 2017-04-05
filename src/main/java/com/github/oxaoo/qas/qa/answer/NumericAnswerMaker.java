package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.ConllGraphComparator;
import com.github.oxaoo.qas.parse.ParseGraph;
import com.github.oxaoo.qas.parse.ParseGraphBuilder;
import com.github.oxaoo.qas.parse.ParseNode;
import com.github.oxaoo.qas.parse.ParserManager;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
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
    private static final Logger LOG = LoggerFactory.getLogger(NumericAnswerMaker.class);

    public static List<Callable<String>> dateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws CreateAnswerException {
        //todo move to up <<<<<
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
        //todo >>>>>

        return sentences.stream()
                .map(s -> (Callable<String>) () -> NumericAnswerMaker.answer(s, headQuestionToken, parser))
                .collect(Collectors.toList());
    }

    private static String answer(String sentence, Conll headQuestionToken, RussianParser parser)
            throws FailedParsingException {
        List<Conll> conlls = parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = ParseGraphBuilder.make(conlls);
        ParseNode<Conll> foundNode = graph.find(headQuestionToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
//        List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
        List<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, 'M');
        return prepareAnswer(dependentNodes);
    }


    private static String prepareAnswer(List<ParseNode<Conll>> dependentNodes) {
        StringBuilder sb = new StringBuilder();
        dependentNodes.stream()
                .map(ParseNode::getValue)
                .sorted(Comparator.comparingInt(Conll::getId))
                .forEach(c -> sb.append(c.getForm()).append(" "));
        return sb.toString();
    }

    private static List<ParseNode<Conll>> findPath2ChildByPos(ParseNode<Conll> parent, char pos) {
        List<ParseNode<Conll>> answerChain = new ArrayList<>();
        findByPos(parent, pos, answerChain);
        return answerChain;
    }

    private static boolean findByPos(ParseNode<Conll> node, char pos, List<ParseNode<Conll>> chain) {
        if (node.getValue().getPosTag() == pos) {
            chain.addAll(node.getAllChild());
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByPos(child, pos, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }
}
