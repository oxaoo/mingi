package com.github.oxaoo.mingi.business.logic;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mingi.business.logic.core.QasEngine;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchEngine;
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
//        Set<String> answers = qasEngine.answer("В каком году затонул Титаник?", new SearchEngine<>(new WebSearchEngine())); //date
//        Set<String> answers = qasEngine.answer("Какая аббревиатура у тринитротолуол?", new SearchEngine<>(new WebSearchEngine())); //abbreviation
//        Set<String> answers = qasEngine.answer("Что значит слово лол?", new SearchEngine<>(new WebSearchEngine())); //abbreviation
//        Set<String> answers = qasEngine.answer("Кто был первым в космосе?", new SearchEngine<>(new WebSearchEngine())); //ind
        Set<String> answers = qasEngine.answer("В каком регионе России находится крупнейший буддистский храм?", new SearchEngine<>(new WebSearchEngine())); //state
//        Set<String> answers = qasEngine.answer("В каком году Медведев стал презедентом?");
//        Set<String> answers = qasEngine.answer("Какое событие произошло в Санкт-Петербурге третьего апреля?", new SearchEngine<>(new WebSearchEngine())); //event
        LOG.info("List of answers:");
        answers.forEach(LOG::info);
        qasEngine.shutdown();
    }
}
