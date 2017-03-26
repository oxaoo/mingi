package com.github.oxaoo.qas.qa;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.training.QuestionModel;
import com.github.oxaoo.qas.training.QuestionToken;
import com.github.oxaoo.qas.training.SvmEngine;
import com.github.oxaoo.qas.utils.PropertyManager;
import libsvm.svm_model;

import java.util.List;
import java.util.Properties;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifier {
    private final svm_model model;
    private final SvmEngine svmEngine;
    private final RussianParser parser;

    private final static String PARSER_CLASSIFIER_MODEL_PROPERTY = "parser.classifier.model.path";
    private final static String PARSER_CONFIG_PATH_PROPERTY = "parser.config.path";
    private final static String PARSER_TREE_TAGGER_HOME_PROPERTY = "parser.tree.tagger.path";


    public QuestionClassifier() throws LoadQuestionClassifierModelException {
        //todo create parser manager for parser and use it
        Properties properties = PropertyManager.getProperties();
        String classifierModelPath = properties.getProperty(PARSER_CLASSIFIER_MODEL_PROPERTY);
        String treeTaggerHome = properties.getProperty(PARSER_TREE_TAGGER_HOME_PROPERTY);
        String parserConfigPath = properties.getProperty(PARSER_CONFIG_PATH_PROPERTY);
        this.parser = new RussianParser(classifierModelPath, treeTaggerHome, parserConfigPath);

        this.model = QuestionClassifierModelLoader.load();
        this.svmEngine = new SvmEngine();
    }

    public QuestionDomain classify(String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        List<String> parsedTokens = this.parser.parse(question);
        QuestionModel questionModel = new QuestionModel();
        for (String token : parsedTokens) {
            Conll conll = Conll.map(token);
            QuestionToken questionToken = QuestionToken.map(conll);
            questionModel.addQuestionToken(questionToken);
        }
        return this.svmEngine.svmClassify(this.model, questionModel);
    }
}
