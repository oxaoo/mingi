package com.github.oxaoo.mingi.core.answer;

import com.github.oxaoo.mingi.core.answer.abbreviation.AbbAnswerMaker;
import com.github.oxaoo.mingi.core.answer.description.DefinitionAnswerMaker;
import com.github.oxaoo.mingi.core.answer.entity.EventAnswerMaker;
import com.github.oxaoo.mingi.core.answer.human.IndAnswerMaker;
import com.github.oxaoo.mingi.core.answer.location.StateAnswerMaker;
import com.github.oxaoo.mingi.core.answer.numeric.DateAnswerMaker;
import com.github.oxaoo.mingi.core.question.QuestionDomain;
import com.github.oxaoo.mingi.search.data.DataFragment;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private final AnswerArbiter answerArbiter;

    public AnswerEngine(RussianParser parser) {
        this.parser = parser;
        this.executor = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        this.answerArbiter = new AnswerArbiter();
    }

    public AnswerEngine(RussianParser parser, int threadPoolSize) {
        this.parser = parser;
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.answerArbiter = new AnswerArbiter();
    }

    public AnswerEngine(RussianParser parser, ExecutorService executor) {
        this.parser = parser;
        this.executor = executor;
        this.answerArbiter = new AnswerArbiter();
    }

    public AnswerEngine(ExecutorService executor, RussianParser parser, AnswerArbiter answerArbiter) {
        this.executor = executor;
        this.parser = parser;
        this.answerArbiter = answerArbiter;
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
        List<String> answers = this.answerExecutor(answerTasks);
        return this.answerArbiter.rank(answers);
    }


    public List<String> answerExecutor(List<Callable<String>> answerTasks) {
        try {
            return executor.invokeAll(answerTasks).stream().map(t -> {
                try {
                    return t.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Exception during answering. Cause: {}", e.getMessage());
                    return "";
                }
            }).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        } catch (InterruptedException e) {
            LOG.error("Exception during invoke concurrent tasks of answering. Cause: {}", e.getMessage());
            return Collections.emptyList();
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
