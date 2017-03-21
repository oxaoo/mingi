package com.github.oxaoo.qas;

import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.qa.QuestionClassifier;
import com.github.oxaoo.qas.search.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws LoadQuestionClassifierModelException {
//        run();
        testSearchEngine();
    }

    private static void testSearchEngine() {
        SearchEngine engine = new SearchEngine();
        engine.find("гора эльбрус");
//        engine.execute();
    }

    private static void run() throws LoadQuestionClassifierModelException {
        QuestionClassifier questionClassifier = new QuestionClassifier();
        questionClassifier.init();
    }
}
