package com.github.oxaoo.mingi.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mingi.business.logic.boundary.QasFacade;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.InitQasEngineException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 01.04.2017
 */
@ApplicationPath("/qas")
@Path("/")
public class QasApi extends Application {
    private final QasFacade facade;

    public QasApi() throws InitQasEngineException {
        super();
        this.facade = new QasFacade();
    }

    @GET
    @Path("/ask")
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

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@DefaultValue("true") @FormDataParam("enabled") boolean enabled,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail) {
        boolean result = this.facade.uploadFile(uploadedInputStream, fileDetail);
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
