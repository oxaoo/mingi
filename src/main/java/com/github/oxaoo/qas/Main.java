package com.github.oxaoo.qas;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.core.QasEngine;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.search.DataFragment;
import com.github.oxaoo.qas.search.RelevantInfoExtractor;
import com.github.oxaoo.qas.search.SearchEngine;
import com.google.api.services.customsearch.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws LoadQuestionClassifierModelException, FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
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

    private static void qas() throws LoadQuestionClassifierModelException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {
        QasEngine qasEngine = new QasEngine();
        List<String> answers = qasEngine.answer("В каком году затонул Титаник?");
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
    }
}
