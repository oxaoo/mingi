package com.github.oxaoo.mingi.core.question;

import com.github.oxaoo.mingi.core.question.training.svm.SvmEngine;
import com.github.oxaoo.mingi.core.question.training.svm.SvmModel;
import com.github.oxaoo.mingi.exceptions.BuildModelException;
import com.github.oxaoo.mingi.exceptions.FindSvmModelException;
import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.exceptions.SaveSvmModelException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Question classifier model loader test.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultQuestionClassifierModelLoaderTest {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultQuestionClassifierModelLoaderTest.class);

    private static final String QAS_HOME_PROPERTY = "qas/";
    private static final String QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY = "qcm.model";

    @InjectMocks
    private DefaultQuestionClassifierModelLoader modelLoader;
    @Mock
    private SvmEngine svmEngine;

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void loadTest() throws LoadQuestionClassifierModelException, FindSvmModelException {
        this.modelLoader.load(QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        verify(this.svmEngine).findModel(anyString());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void loadFindExceptionTest() throws LoadQuestionClassifierModelException, FindSvmModelException, SaveSvmModelException {
        when(this.svmEngine.findModel(anyString())).thenThrow(new FindSvmModelException());
        this.modelLoader.load(QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        verify(this.svmEngine).findModel(anyString());
        verify(this.svmEngine).saveModel(any(SvmModel.class), anyString());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void buildTest() throws BuildModelException, SaveSvmModelException {
        this.modelLoader.build(QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        verify(this.svmEngine).saveModel(any(SvmModel.class), anyString());
    }

    @Test(expected = BuildModelException.class)
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void buildSaveModelExceptionTest() throws BuildModelException, SaveSvmModelException {
        doThrow(new SaveSvmModelException()).when(this.svmEngine).saveModel(any(SvmModel.class), anyString());
        this.modelLoader.build(QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
    }
}
