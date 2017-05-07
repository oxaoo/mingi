package com.github.oxaoo.qas.business.logic.qa.question;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.business.logic.qa.question.QuestionClassifier;
import com.github.oxaoo.qas.business.logic.training.QuestionModel;
import com.github.oxaoo.qas.business.logic.training.SvmEngine;
import libsvm.svm_model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

/**
 * The type Question classifier test.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionClassifierTest {

    @InjectMocks
    private QuestionClassifier classifier;
    @Mock
    private static RussianParser parser;
    @Mock
    private SvmEngine svmEngine;
    @Mock
    private svm_model svmModel;

    /**
     * Classify test.
     *
     * @throws FailedParsingException          the failed parsing exception
     * @throws FailedConllMapException         the failed conll map exception
     * @throws FailedQuestionTokenMapException the failed question token map exception
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void classifyTest() throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        String question = "the question stub";
        classifier.classify(question);

        Mockito.verify(parser).parse(question, Conll.class);
    }

    /**
     * Classify with args test.
     *
     * @throws FailedConllMapException         the failed conll map exception
     * @throws FailedQuestionTokenMapException the failed question token map exception
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    public void classifyWithArgsTest() throws FailedConllMapException, FailedQuestionTokenMapException {
        List<Conll> conllTokens = Collections.emptyList();
        this.classifier.classify(conllTokens);

        Mockito.verify(this.svmEngine).svmClassify(Mockito.eq(this.svmModel), Mockito.any(QuestionModel.class));
    }
}
