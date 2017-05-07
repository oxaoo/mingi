package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.qas.business.logic.exceptions.BuildModelException;
import com.github.oxaoo.qas.business.logic.exceptions.FindSvmModelException;
import com.github.oxaoo.qas.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.business.logic.exceptions.SaveSvmModelException;
import com.github.oxaoo.qas.business.logic.training.SvmEngine;
import libsvm.svm_model;
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

    @InjectMocks
    private DefaultQuestionClassifierModelLoader modelLoader;
    @Mock
    private SvmEngine svmEngine;

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void loadTest() throws LoadQuestionClassifierModelException, FindSvmModelException {
        this.modelLoader.load();
        verify(this.svmEngine).findModel(anyString());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void loadFindExceptionTest() throws LoadQuestionClassifierModelException, FindSvmModelException, SaveSvmModelException {
        when(this.svmEngine.findModel(anyString())).thenThrow(new FindSvmModelException());
        this.modelLoader.load();
        verify(this.svmEngine).findModel(anyString());
        verify(this.svmEngine).saveModel(any(svm_model.class), anyString());
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void buildTest() throws BuildModelException, SaveSvmModelException {
        this.modelLoader.build();
        verify(this.svmEngine).saveModel(any(svm_model.class), anyString());
    }

    @Test(expected = BuildModelException.class)
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void buildSaveModelExceptionTest() throws BuildModelException, SaveSvmModelException {
        doThrow(new SaveSvmModelException()).when(this.svmEngine).saveModel(any(svm_model.class), anyString());
        this.modelLoader.build();
    }
}
