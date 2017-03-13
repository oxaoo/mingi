package com.github.oxaoo.qas.qa;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.qas.training.TrainingModel;
import com.github.oxaoo.qas.training.TrainingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws FailedParsingException {
//        parsing();
        training();
    }

    private static void training() {
        TrainingUtils utils = new TrainingUtils();
        List<TrainingModel> trainingModels = utils.readTrainingSet("src/main/resources/qa/training.parse");
//        LOG.info("Training set: " + trainingModels.toString());
        TrainingModel tm = trainingModels.get(0);
        LOG.info("TM before sort:" + tm.toString());
        tm.sortByHead();
        LOG.info("TM after  sort:" + tm.toString());
    }

    private static void parsing() throws FailedParsingException {
        String parserDir = "src/main/resources/parser/";
        String textFilePath = parserDir + "text.txt";
        String classifierModel = parserDir + "russian-utf8.par";
        String resultParseFile = new RussianParser().parsing(textFilePath, classifierModel, parserDir, parserDir);
        LOG.info("Result of parse file: " + resultParseFile);
    }
}
