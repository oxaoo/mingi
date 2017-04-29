package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        RussianParser parser;
        try {
            parser = ParserManager.getParser();
        } catch (ProvideParserException e) {
            throw new CreateAnswerException("Could not create an answer for a question of type DATE.", e);
        }
        questionTokens = questionTokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        //find the verb
        final Conll targetToken;
        if (questionTokens.get(0).getPosTag() == 'V') {
            targetToken = questionTokens.get(0);
        } else {
            Conll foundToken = null;
            for (Conll token : questionTokens) {
                if (token.getPosTag() == 'V') {
                    foundToken = token;
                    break;
                }
            }
            if (foundToken != null) targetToken = foundToken;
            else targetToken = questionTokens.get(0);
        }

        List<String> sentences = dataFragments.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());

        return sentences.stream()
                .map(s -> (Callable<String>) () -> NumericAnswerMaker.answer(s, targetToken, parser))
                .collect(Collectors.toList());
    }

    private static String answer(String sentence, Conll headQuestionToken, RussianParser parser)
            throws FailedParsingException {
        List<Conll> conlls = parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = new ConllParseGraphBuilder().build(conlls);
        ParseNode<Conll> foundNode = graph.find(headQuestionToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
//        List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
        Set<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, 'M');
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

    private static Set<ParseNode<Conll>> findPath2ChildByPos(ParseNode<Conll> parent, char pos) {
        Set<ParseNode<Conll>> answerChain = new HashSet<>();
        findByPos(parent, pos, answerChain);
//        Optional<ParseNode<Conll>> optionalNode
//                = answerChain.stream().filter(n -> n.getValue().getPosTag() == pos).findFirst();
//        if (optionalNode.isPresent()) {
//            ParseNode<Conll> foundNode = optionalNode.get();
//            findByPos(foundNode, pos, answerChain);
//        }
        return answerChain;
    }

    private static boolean findByPos(ParseNode<Conll> node, char pos, Set<ParseNode<Conll>> chain) {
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
