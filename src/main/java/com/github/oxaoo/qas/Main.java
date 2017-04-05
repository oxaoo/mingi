package com.github.oxaoo.qas;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.core.QasEngine;
import com.github.oxaoo.qas.exceptions.CreateAnswerException;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.InitQasEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException, InitQasEngineException, CreateAnswerException {
//        testSearchEngine();
        qas();
    }

    private static void testSearchEngine() {
//        SearchEngine engine = new SearchEngine();
//        LOG.info("Simple invoke find sources:");
//        List<Result> results = engine.find("где родился Пушкин?");
//        List<DataFragment> relevantFragments = RelevantInfoExtractor.extract(results);
//        LOG.info("End the simple invoke find sources. Number of result: {}", relevantFragments.size());
////        relevantFragments.forEach(s -> LOG.info("### {}", s));
//
//        LOG.info("Concurrent invoke find sources:");
//        List<Result> results2 = engine.find("где родился Пушкин?");
//        List<DataFragment> relevantFragments2 = RelevantInfoExtractor.concurrentExtract(results2);
//        LOG.info("End the concurrent invoke find sources. Number of result: {}", relevantFragments2.size());

    }

    private static void qas() throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException,
            CreateAnswerException,
            InitQasEngineException {
        QasEngine qasEngine = new QasEngine();
        Set<String> answers = qasEngine.answer("В каком году затонул Титаник?");
//        Set<String> answers = qasEngine.answer("В какой войне была использована первая подводная лодка?");
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
    }
}
