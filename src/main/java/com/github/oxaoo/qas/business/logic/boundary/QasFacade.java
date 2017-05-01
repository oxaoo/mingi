package com.github.oxaoo.qas.business.logic.boundary;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.business.logic.common.FileManager;
import com.github.oxaoo.qas.business.logic.core.QasEngine;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.SearchFactory;
import com.github.oxaoo.qas.business.logic.search.engine.enterprise.EnterpriseSearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.web.WebSearchEngine;
import com.sun.jersey.core.header.FormDataContentDisposition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.05.2017
 */
@Getter
@AllArgsConstructor
public class QasFacade {
    private final QasEngine qasEngine;
    private final FileManager fileManager;

    public QasFacade() throws InitQasEngineException {
        this.qasEngine = new QasEngine();
        this.fileManager = new FileManager();
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

    public boolean uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
        return this.fileManager.saveFile(uploadedInputStream, fileDetail);
    }
}
