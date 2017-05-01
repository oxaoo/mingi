package com.github.oxaoo.qas.business.logic.boundary;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.business.logic.core.QasEngine;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.SearchFactory;
import com.github.oxaoo.qas.business.logic.search.engine.enterprise.EnterpriseSearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.web.WebSearchEngine;

import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.05.2017
 */
public class QasFacade {
    private final QasEngine qasEngine;

    public QasFacade() throws InitQasEngineException {
        this.qasEngine = new QasEngine();
    }

    public QasFacade(QasEngine qasEngine) {
        this.qasEngine = qasEngine;
    }

    public Set<String> askQuestion(String question, boolean isWebSearch)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        SearchFactory<?, ?> searchFactory = this.provideSearchFactory(isWebSearch);
        return this.qasEngine.answer(question, new SearchEngine<>(searchFactory));
    }

    private SearchFactory<?, ?> provideSearchFactory(boolean isWebSearch) {
        if (isWebSearch) {
            return new WebSearchEngine();
        } else {
            return new EnterpriseSearchEngine();
        }
    }

    //todo impl
    public boolean uploadFile(String filePath) {
        return true;
    }
}
