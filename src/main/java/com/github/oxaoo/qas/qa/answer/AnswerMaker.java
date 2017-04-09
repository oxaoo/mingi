package com.github.oxaoo.qas.qa.answer;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.qa.QuestionDomain;
import com.github.oxaoo.qas.search.DataFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.03.2017
 */
public class AnswerMaker {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerMaker.class);

    private final static int DEFAULT_THREAD_POOL_SIZE = 10;
    private final ExecutorService executor;

    public AnswerMaker() {
        this.executor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    public AnswerMaker(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public AnswerMaker(ExecutorService executor) {
        this.executor = executor;
    }

    public Set<String> make(List<Conll> questionTokens,
                            QuestionDomain questionDomain,
                            List<DataFragment> dataFragments) throws CreateAnswerException {
        List<Callable<String>> answerTasks;
        switch (questionDomain) {
            //NUMERIC
            case DATE:
                answerTasks = NumericAnswerMaker.dateAnswer(questionTokens, dataFragments);
                break;
            //LOCATION
            case STATE:
                answerTasks = LocationAnswerMaker.stateAnswer(questionTokens, dataFragments);
                break;
            //ENTITY
            case EVENT:
                answerTasks = EntityAnswerMaker.eventAnswer(questionTokens, dataFragments);
                break;
            //HUMAN
            case IND:
                answerTasks = HumanAnswerMaker.indAnswer(questionTokens, dataFragments);
                break;
            //ABBREVIATION
            case ABB:
                answerTasks = AbbreviationAnswerMaker.abbAnswer(questionTokens, dataFragments);
                break;
                //DESCRIPTION
            case DEFINITION:
                answerTasks = DescriptionAnswerMaker.definitionAnswer(questionTokens, dataFragments);
                break;

            default:
                LOG.error("Incorrect question domain: {}", questionDomain.name());
                return Collections.emptySet();
        }

        return this.answerExecutor(answerTasks);
    }

    public Set<String> answerExecutor(List<Callable<String>> answerTasks) {
        try {
            return executor.invokeAll(answerTasks).stream().map(t -> {
                try {
                    return t.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Exception during answering. Cause: {}", e.getMessage());
                    return "";
                }
            }).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        } catch (InterruptedException e) {
            LOG.error("Exception during invoke concurrent tasks of answering. Cause: {}", e.getMessage());
            return Collections.emptySet();
        }
    }

    public void shutdownExecutor() {
        try {
            LOG.debug("Attempt to shutdown the Answer Executor");
            this.executor.shutdown();
            this.executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.warn("The shutdown answer task is interrupted. Cause: {}", e.getMessage());
        } finally {
            if (!this.executor.isTerminated()) {
                LOG.error("To cancel the non-finished answer tasks");
            }
            this.executor.shutdownNow();
            LOG.debug("Answer Executor shutdown finished");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.shutdownExecutor();
    }
}
