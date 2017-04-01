package com.github.oxaoo.qas.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.core.QasEngine;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.04.2017
 */
@Path("/")
public class QasService {
    private final QasEngine qasEngine;

    public QasService() throws LoadQuestionClassifierModelException {
        this.qasEngine = new QasEngine();
    }

    @GET
    @Path("/ask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ask(@QueryParam("question") String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException, JsonProcessingException {
//        String answer = "You ask: " + question;
        Set<String> answers = this.qasEngine.answer(question);
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(answers);
        return Response.ok(str).build();
    }
}
