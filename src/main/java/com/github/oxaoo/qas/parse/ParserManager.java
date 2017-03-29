package com.github.oxaoo.qas.parse;

import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.qas.utils.PropertyManager;

import java.util.Properties;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 29.03.2017
 */
public class ParserManager {
    private static volatile ParserManager parserManager;

    private final RussianParser parser;

    private final static String PARSER_CLASSIFIER_MODEL_PROPERTY = "parser.classifier.model.path";
    private final static String PARSER_CONFIG_PATH_PROPERTY = "parser.config.path";
    private final static String PARSER_TREE_TAGGER_HOME_PROPERTY = "parser.tree.tagger.path";

    private ParserManager() {
        Properties properties = PropertyManager.getProperties();
        String classifierModelPath = properties.getProperty(PARSER_CLASSIFIER_MODEL_PROPERTY);
        String treeTaggerHome = properties.getProperty(PARSER_TREE_TAGGER_HOME_PROPERTY);
        String parserConfigPath = properties.getProperty(PARSER_CONFIG_PATH_PROPERTY);
        this.parser = new RussianParser(classifierModelPath, treeTaggerHome, parserConfigPath);
    }

    public static RussianParser getParser() {
        if (parserManager == null) {
            synchronized (ParserManager.class) {
                if (parserManager == null) {
                    parserManager = new ParserManager();
                }
            }
        }
        return parserManager.parser;
    }
}
