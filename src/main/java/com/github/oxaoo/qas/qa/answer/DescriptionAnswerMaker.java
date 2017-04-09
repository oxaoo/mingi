package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.ProvideParserException;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * The Description answer maker present handles domains questions of the following type:
 * DEFINITION,
 * DESCRIPTION,
 * MANNER,
 * REASON,
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 09.04.2017
 */
public class DescriptionAnswerMaker {
    public static List<Callable<String>> definitionAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws CreateAnswerException {
        RussianParser parser;
        try {
            parser = ParserManager.getParser();
        } catch (ProvideParserException e) {
                throw new CreateAnswerException("Could not create an answer for a question of type DEFINITION.", e);
        }

        Conll targetToken = AnswerMakerTools.getRoot(questionTokens, "Nc.*");

        List<String> sentences = dataFragments.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());

        return sentences.stream()
                .map(s -> (Callable<String>) () -> DescriptionAnswerMaker.answer(s, targetToken, parser))
                .collect(Collectors.toList());
    }

    private static String answer(String sentence, Conll targetToken, RussianParser parser)
            throws FailedParsingException {
        List<Conll> conlls = parser.parseSentence(sentence, Conll.class);
        ParseGraph<Conll> graph = ParseGraphBuilder.make(conlls);
        ParseNode<Conll> foundNode = graph.find(targetToken, new ConllGraphComparator());
        //skip the fragments which doesn't contain the necessary information
        if (foundNode == null) {
            return "";
        }
        List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
        return prepareAnswer(new HashSet<>(dependentNodes));
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
