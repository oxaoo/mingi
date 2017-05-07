package com.github.oxaoo.qas.business.logic.boundary;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.qas.business.logic.common.FileManager;
import com.github.oxaoo.qas.business.logic.core.QasEngine;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.exceptions.InitQasEngineException;
import com.github.oxaoo.qas.business.logic.search.engine.SearchEngine;
import com.sun.jersey.core.header.FormDataContentDisposition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;

/**
 * @author <a href="mailto:aleksandr.kuleshov@t-systems.ru">Alexander Kuleshov</a>
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
     * Init test.
     * Check that all facade components are initialized.
     *
     * @throws InitQasEngineException the init qas engine exception
     */
    @Test
    public void initTest() throws InitQasEngineException {
        QasFacade facade = new QasFacade();
        Assert.assertNotNull(facade.getQasEngine());
        Assert.assertNotNull(facade.getFileManager());
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
