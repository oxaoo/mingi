package com.github.oxaoo.mingi.business.logic.boundary;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mingi.business.logic.common.FileManager;
import com.github.oxaoo.mingi.business.logic.core.QasEngine;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.search.engine.SearchEngine;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class QasFacadeTest {
    private static QasEngine engineMock;
    private static FileManager fileManager;
    private static QasFacade facade;

    @BeforeClass
    public static void setUp() {
        engineMock = Mockito.mock(QasEngine.class);
        fileManager = Mockito.mock(FileManager.class);
        facade = new QasFacade(engineMock, fileManager);
    }

    /**
     * Ask question test.
     * <p>
     * Check that the answer method is always called.
     *
     * @throws FailedParsingException          the failed parsing exception
     * @throws FailedConllMapException         the failed conll map exception
     * @throws FailedQuestionTokenMapException the failed question token map exception
     */
    @Test
    @SuppressWarnings({"unchecked", "PMD.JUnitTestsShouldIncludeAssert"})
    public void askQuestionTest() throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        String question = "the question";
        facade.askQuestion(question, true);
        Mockito.verify(engineMock).answer(Mockito.eq(question), Mockito.any(SearchEngine.class));
    }

    /**
     * Upload file test.
     * <p>
     * Check that the saveFile method is always called.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void uploadFileTest() {
        InputStream uploadedInputStream = Mockito.mock(InputStream.class);
        FormDataContentDisposition fileDetail = Mockito.mock(FormDataContentDisposition.class);
        facade.uploadFile(uploadedInputStream, fileDetail);
        Mockito.verify(fileManager).saveFile(uploadedInputStream, fileDetail);
    }
}
