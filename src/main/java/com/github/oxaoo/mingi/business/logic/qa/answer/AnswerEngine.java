package com.github.oxaoo.mingi.business.logic.qa.answer;

import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.business.logic.qa.answer.abbreviation.AbbAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.answer.description.DefinitionAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.answer.entity.EventAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.answer.human.IndAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.answer.location.StateAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.answer.numeric.DateAnswerMaker;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionDomain;
import com.github.oxaoo.mingi.business.logic.search.data.DataFragment;
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
public class AnswerEngine {
    private static final Logger LOG = LoggerFactory.getLogger(AnswerEngine.class);

    private final static int DEFAULT_THREAD_POOL_SIZE = 10;
    private final ExecutorService executor;
    private final RussianParser parser;

    public AnswerEngine(RussianParser parser) {
        this.parser = parser;
        this.executor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    public AnswerEngine(RussianParser parser, int threadPoolSize) {
        this.parser = parser;
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public AnswerEngine(RussianParser parser, ExecutorService executor) {
        this.parser = parser;
        this.executor = executor;
    }

    public Set<String> make(List<Conll> questionTokens,
                            QuestionDomain questionDomain,
                            List<DataFragment> dataFragments) {
        AnswerContext<String, Conll, DataFragment> answerContext = new AnswerContext<>();
        switch (questionDomain) {
            //NUMERIC
            case DATE:
                answerContext.setAnswerMaker(new DateAnswerMaker());
                break;
            //LOCATION
            case STATE:
                answerContext.setAnswerMaker(new StateAnswerMaker());
                break;
            //ENTITY
            case EVENT:
                answerContext.setAnswerMaker(new EventAnswerMaker());
                break;
            //HUMAN
            case IND:
                answerContext.setAnswerMaker(new IndAnswerMaker());
                break;
            //ABBREVIATION
            case ABB:
                answerContext.setAnswerMaker(new AbbAnswerMaker());
                break;
            //DESCRIPTION
            case DEFINITION:
                answerContext.setAnswerMaker(new DefinitionAnswerMaker());
                break;

            default:
                LOG.error("Incorrect question domain: {}", questionDomain.name());
                return Collections.emptySet();
        }
        answerContext.setParser(this.parser);
        List<Callable<String>> answerTasks = answerContext.runAnswerMaker(questionTokens, dataFragments);
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
