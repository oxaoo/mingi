package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * The Entity answer maker present handles domains questions of the following type:
 * ANIMAL,
 * BODY,
 * COLOR,
 * CREATIVE,
 * CURRENCY,
 * DIS_MED,
 * EVENT,
 * FOOD,
 * INSTRUMENT,
 * LANG,
 * LETTER,
 * OTHER_ENTITY,
 * PLANT,
 * PRODUCT,
 * RELIGION,
 * SPORT,
 * SUBSTANCE,
 * SYMBOL,
 * TECHNIQUE,
 * TERM,
 * VEHICLE,
 * WORD,
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 06.04.2017
 */
public class EntityAnswerMaker {

    public static List<Callable<String>> eventAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
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

        return sentences.stream()
                .map(s -> (Callable<String>) () -> EntityAnswerMaker.answer(s, headQuestionToken, parser))
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
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, Arrays.asList('N', 'V'));
        return prepareAnswer(dependentNodes);
    }

    private static String prepareAnswer(Set<ParseNode<Conll>> dependentNodes) {
        StringBuilder sb = new StringBuilder();
        dependentNodes.stream()
                .map(ParseNode::getValue)
                .sorted(Comparator.comparingInt(Conll::getId))
                .forEach(c -> sb.append(c.getForm()).append(" "));
        return sb.toString();
    }

    private static Set<ParseNode<Conll>> findPath2ChildByPos(ParseNode<Conll> parent, List<Character> posList) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByPos(parent, posList, answerChain);
        return answerChain;
    }

    private static boolean findByPos(ParseNode<Conll> node, List<Character> posList, Set<ParseNode<Conll>> chain) {
        if (posList.contains(node.getValue().getPosTag())) {
//            chain.addAll(node.getAllChild());
            chain.addAll(foo(node, new ArrayList<>()));
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByPos(child, posList, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }

    private static List<ParseNode<Conll>> foo(ParseNode<Conll> node, List<ParseNode<Conll>> childChain) {
        for (ParseNode<Conll> child : node.getChildren()) {
            childChain.add(child);
            foo(child, childChain);
        }
        return childChain;
    }
}
