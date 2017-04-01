package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.parse.*;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
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

    public static Set<String> concurrentDateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws FailedParsingException {
        RussianParser parser = ParserManager.getParser();
        questionTokens = questionTokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        Conll headQuestionToken = questionTokens.get(0);

        List<String> sentences = dataFragments.stream()
                .map(DataFragment::getRelevantInfoList).flatMap(List::stream)
                .map(RelevantInfo::getRelevantSentences).flatMap(List::stream)
                .collect(Collectors.toList());

        List<Callable<String>> answerTasks = sentences.stream()
                .map(s -> (Callable<String>) () -> NumericAnswerMaker.answer(s, headQuestionToken, parser))
                .collect(Collectors.toList());
        ExecutorService executor = Executors.newFixedThreadPool(dataFragments.size());

        try {
            return executor.invokeAll(answerTasks).stream().map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Exception during answering. Cause: {}", e.getMessage());
                    return "";
                }
            }).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        } catch (InterruptedException e) {
            LOG.error("Exception during invoke concurrent tasks of answering. Cause: {}", e.getMessage());
            return Collections.emptySet();
        } finally {
            shutdownExecutor(executor);
        }
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

    private static void shutdownExecutor(ExecutorService executor) {
        try {
            LOG.debug("Attempt to shutdown the Answer Executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.warn("The shutdown answer task is interrupted. Cause: {}", e.getMessage());
        } finally {
            if (!executor.isTerminated()) {
                LOG.error("To cancel the non-finished answer tasks");
            }
            executor.shutdownNow();
            LOG.debug("Answer Executor shutdown finished");
        }
    }


    @Deprecated
    public static Set<String> dateAnswer(List<Conll> questionTokens, List<DataFragment> dataFragments)
            throws FailedParsingException {
        RussianParser parser = ParserManager.getParser();
        Set<String> answers = new HashSet<>();
        questionTokens = questionTokens.stream()
                .sorted(Comparator.comparingInt(Conll::getHead))
                .collect(Collectors.toList());
        Conll headQuestionToken = questionTokens.get(0);

        //foreach pages
        for (DataFragment dataFragment : dataFragments) {
            //foreach snippets of pages
            for (RelevantInfo relevantInfo : dataFragment.getRelevantInfoList()) {
                //foreach sentences corresponding the snippets
                for (String sentence : relevantInfo.getRelevantSentences()) {
                    List<Conll> conlls = parser.parseSentence(sentence, Conll.class);
                    ParseGraph<Conll> graph = ParseGraphBuilder.make(conlls);
                    ParseNode<Conll> foundNode = graph.find(headQuestionToken, new ConllGraphComparator());
                    //skip the fragments which doesn't contain the necessary information
                    if (foundNode == null) {
                        continue;
                    }
//                    List<ParseNode<Conll>> dependentNodes = foundNode.getAllChild();
                    List<ParseNode<Conll>> dependentNodes = findPath2ChildByPos(foundNode, 'M');
                    answers.add(prepareAnswer(dependentNodes));
                }
            }
        }
        return answers;
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
            chain.add(node);
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
