package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * The Human answer maker present handles domains questions of the following type:
 * GROUP,
 * IND,
 * TITLE,
 * DESCRIPTION_HUMAN
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 08.04.2017
 */
public class HumanAnswerMaker {

    public static List<Callable<String>> indAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
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
                .map(s -> (Callable<String>) () -> HumanAnswerMaker.answer(s, headQuestionToken, parser))
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
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByStartFeats(foundNode, "Np");
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

    private static Set<ParseNode<Conll>> findPath2ChildByStartFeats(ParseNode<Conll> parent, String startFeats) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByFeats(parent, startFeats, answerChain);
        return answerChain;
    }

    private static boolean findByFeats(ParseNode<Conll> node, String startFeats, Set<ParseNode<Conll>> chain) {
        if (node.getValue().getFeats().startsWith(startFeats)) {
            chain.addAll(node.getAllChild());
            return true;
        } else if (!node.getChildren().isEmpty()) {
            for (ParseNode<Conll> child : node.getChildren()) {
                boolean isFound = findByFeats(child, startFeats, chain);
                if (isFound) {
                    chain.add(node);
                    return true;
                }
            }
        } else return false;
        return false;
    }
}
