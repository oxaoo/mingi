package com.github.oxaoo.qas;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.core.QasEngine;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.InitQasEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException,
            InitQasEngineException {
//        testSearchEngine();
        qas();
    }

    private static void qas() throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException,
            InitQasEngineException {
        QasEngine qasEngine = new QasEngine();
        Set<String> answers = qasEngine.answer("В каком году затонул Титаник?"); //+ DATE
//        Set<String> answers = qasEngine.answer("В каком регионе России выпадает наибольшее количество осадков в год?");
//        Set<String> answers = qasEngine.answer("В каком регионе России находится крупнейший буддистский храм?"); //+ STATE
//        Set<String> answers = qasEngine.answer("В каком году Медведев стал презедентом?");
//        Set<String> answers = qasEngine.answer("Какое событие произошло в Санкт-Петербурге третьего апреля?");
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
        qasEngine.shutdown();
    }
}
