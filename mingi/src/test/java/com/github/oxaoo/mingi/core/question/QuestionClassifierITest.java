package com.github.oxaoo.mingi.core.question;

import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mingi.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.exceptions.ProvideParserException;

/**
 * The type Question classifier i test.
 *
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
//fixme
public class QuestionClassifierITest {
    /*private static final Logger LOG = LoggerFactory.getLogger(QuestionClassifierITest.class);

    private static final String QAS_HOME_PROPERTY = "qas/";
    private static final String QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY = "qcm.model";

    *//**
     * Init with parser and model loader test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     *//*
    @Test
    public void initWithParserAndModelLoaderTest() throws ProvideParserException, LoadQuestionClassifierModelException {
        RussianParser parser = ParserManager.getParser();
        QuestionClassifier classifier = new QuestionClassifier(parser, QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);

        Assert.assertNotNull(classifier.getParser());
        Assert.assertNotNull(classifier.getSvmEngine());
        Assert.assertNotNull(classifier.getModel());
    }

    *//**
     * Classify test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     * @throws FailedParsingException               the failed parsing exception
     * @throws FailedConllMapException              the failed conll map exception
     * @throws FailedQuestionTokenMapException      the failed question token map exception
     *//*
    @Test
    public void classifyTest() throws ProvideParserException, LoadQuestionClassifierModelException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        RussianParser parser = ParserManager.getParser();
        QuestionClassifier classifier = new QuestionClassifier(parser, QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        String question = "В каком году затонул Титаник?";
        QuestionDomain domain = classifier.classify(question);
        LOG.info("Domain: {}", domain);
        Assert.assertNotNull(domain);
    }


    *//**
     * Classify by tokens test.
     *
     * @throws ProvideParserException               the provide parser exception
     * @throws LoadQuestionClassifierModelException the load question classifier model exception
     * @throws FailedParsingException               the failed parsing exception
     * @throws FailedConllMapException              the failed conll map exception
     * @throws FailedQuestionTokenMapException      the failed question token map exception
     *//*
    @Test
    public void classifyByTokensTest() throws ProvideParserException, LoadQuestionClassifierModelException,
            FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        String question = "В каком году затонул Титаник?";
        RussianParser parser = ParserManager.getParser();
        QuestionClassifier classifier = new QuestionClassifier(parser, QAS_HOME_PROPERTY + QUESTION_CLASSIFIER_MODEL_PATH_PROPERTY);
        List<Conll> conllTokens = parser.parse(question, Conll.class);
        QuestionDomain domain = classifier.classify(conllTokens);
        LOG.info("Domain: {}", domain);
        Assert.assertNotNull(domain);
    }*/
}
