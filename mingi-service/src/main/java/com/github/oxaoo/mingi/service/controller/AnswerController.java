package com.github.oxaoo.mingi.service.controller;

import com.github.oxaoo.mingi.business.logic.core.QasEngine;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchEngine;
import com.github.oxaoo.mingi.business.logic.search.engine.web.WebSearchUnit;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.google.api.services.customsearch.model.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 0.2.0
 * @since 10.05.2018
 */
@RestController
@RequestMapping(value = "/ask")
public class AnswerController {

    private QasEngine qasEngine;
    private SearchEngine<?, ?> searchEngine;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity post(@RequestBody String question) {
        try {
            final Set<String> answers = this.qasEngine.answer(question, this.searchEngine);
            return ResponseEntity.ok(answers);
        } catch (final FailedQuestionTokenMapException | FailedConllMapException | FailedParsingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.getStackTrace(e));
        }
    }

    @Autowired
    public void setQasEngine(final QasEngine qasEngine) {
        this.qasEngine = qasEngine;
    }

    @Autowired
    public void setSearchEngine(final SearchEngine<?, ?> searchEngine) {
        this.searchEngine = searchEngine;
    }
}
