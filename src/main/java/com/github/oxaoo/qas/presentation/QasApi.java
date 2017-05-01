package com.github.oxaoo.qas.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.business.logic.boundary.QasFacade;
import com.github.oxaoo.qas.business.logic.core.QasEngine;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.business.logic.search.engine.SearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.SearchFactory;
import com.github.oxaoo.qas.business.logic.search.engine.enterprise.EnterpriseSearchEngine;
import com.github.oxaoo.qas.business.logic.search.engine.web.WebSearchEngine;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.04.2017
 */
@ApplicationPath("/qas")
@Path("/ask")
public class QasApi extends Application {
    private final QasFacade facade;

    public QasApi() throws InitQasEngineException {
        super();
        this.facade = new QasFacade();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response ask(@QueryParam("question") @NotNull @Size(min = 4) String question,
                        @QueryParam("webSearch") @DefaultValue("true") boolean webSearch)
            throws FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException,
            JsonProcessingException {
        Set<String> answers = this.facade.askQuestion(question, webSearch);
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(answers);
        return Response.ok(str).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@QueryParam("filePath") @NotNull String filePath) {
        boolean result = this.facade.uploadFile(filePath);
        if (result) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Collections.singletonList(QasApi.class));
    }
}
