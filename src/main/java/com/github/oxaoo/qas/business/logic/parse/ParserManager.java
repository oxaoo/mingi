package com.github.oxaoo.qas.business.logic.parse;

import com.github.oxaoo.mp4ru.exceptions.InitRussianParserException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.qas.business.logic.exceptions.InitParserManagerException;
import com.github.oxaoo.qas.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.qas.business.logic.utils.PropertyManager;

import java.util.Properties;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.03.2017
 */
public class ParserManager {
    private static volatile ParserManager parserManager;

    private RussianParser parser;

    private final static String PARSER_CLASSIFIER_MODEL_PROPERTY = "parser.classifier.model.path";
    private final static String PARSER_CONFIG_PATH_PROPERTY = "parser.config.path";
    private final static String PARSER_TREE_TAGGER_HOME_PROPERTY = "parser.tree.tagger.path";

    private ParserManager() throws InitParserManagerException {
        Properties properties = PropertyManager.getProperties();
        String classifierModelPath = properties.getProperty(PARSER_CLASSIFIER_MODEL_PROPERTY);
        String treeTaggerHome = properties.getProperty(PARSER_TREE_TAGGER_HOME_PROPERTY);
        String parserConfigPath = properties.getProperty(PARSER_CONFIG_PATH_PROPERTY);

        try {
            parser = new RussianParser(classifierModelPath, treeTaggerHome, parserConfigPath);
        } catch (InitRussianParserException e) {
            throw new InitParserManagerException("An error occurred while initializing the Parser Manager.", e);
        }
    }

    public static RussianParser getParser() throws ProvideParserException {
        if (parserManager == null) {
            synchronized (ParserManager.class) {
                if (parserManager == null) {
                    try {
                        parserManager = new ParserManager();
                    } catch (InitParserManagerException e) {
                        throw new ProvideParserException("Could not provide a parser instance.", e);
                    }
                }
            }
        }
        return parserManager.parser;
    }
}
