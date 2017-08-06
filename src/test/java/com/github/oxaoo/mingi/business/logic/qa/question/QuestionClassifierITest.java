package com.github.oxaoo.mingi.business.logic.qa.question;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.mingi.business.logic.parse.ParserManager;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The type Question classifier i test.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public class QuestionClassifierITest {
    private static final Logger LOG = LoggerFactory.getLogger(QuestionClassifierITest.class);

    /**
     * Init test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     */
    @Test
    public void initTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        QuestionClassifier classifier = new QuestionClassifier();

        Assert.assertNotNull(classifier.getParser());
        Assert.assertNotNull(classifier.getSvmEngine());
        Assert.assertNotNull(classifier.getModel());
    }

    /**
     * Init with args test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     */
    @Test
    public void initWithParserTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        RussianParser parser = ParserManager.getParser();
        QuestionClassifier classifier = new QuestionClassifier(parser);

        Assert.assertNotNull(classifier.getParser());
        Assert.assertNotNull(classifier.getSvmEngine());
        Assert.assertNotNull(classifier.getModel());
    }

    /**
     * Init with model loader test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     */
    @Test
    public void initWithModelLoaderTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        QuestionClassifier classifier = new QuestionClassifier(modelLoader);

        Assert.assertNotNull(classifier.getParser());
        Assert.assertNotNull(classifier.getSvmEngine());
        Assert.assertNotNull(classifier.getModel());
    }

    /**
     * Init with parser and model loader test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     */
    @Test
    public void initWithParserAndModelLoaderTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        RussianParser parser = ParserManager.getParser();
        QuestionClassifierModelLoader modelLoader = new DefaultQuestionClassifierModelLoader();
        QuestionClassifier classifier = new QuestionClassifier(modelLoader, parser);

        Assert.assertNotNull(classifier.getParser());
        Assert.assertNotNull(classifier.getSvmEngine());
        Assert.assertNotNull(classifier.getModel());
    }

    /**
     * Classify test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     * @throws FailedParsingException               the failed parsing exception
     * @throws FailedConllMapException              the failed conll map exception
     * @throws FailedQuestionTokenMapException      the failed question token map exception
     */
    @Test
    public void classifyTest() throws ProvideParserException, LoadQuestionClassifierModelException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        QuestionClassifier classifier = new QuestionClassifier();
        String question = "В каком году затонул Титаник?";
        QuestionDomain domain = classifier.classify(question);
        LOG.info("Domain: {}", domain);
        Assert.assertNotNull(domain);
    }


    /**
     * Classify by tokens test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     * @throws FailedParsingException               the failed parsing exception
     * @throws FailedConllMapException              the failed conll map exception
     * @throws FailedQuestionTokenMapException      the failed question token map exception
     */
    @Test
    public void classifyByTokensTest() throws ProvideParserException, LoadQuestionClassifierModelException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        QuestionClassifier classifier = new QuestionClassifier();
        String question = "В каком году затонул Титаник?";
        RussianParser parser = ParserManager.getParser();
        List<Conll> conllTokens = parser.parse(question, Conll.class);
        QuestionDomain domain = classifier.classify(conllTokens);
        LOG.info("Domain: {}", domain);
        Assert.assertNotNull(domain);
    }
}
