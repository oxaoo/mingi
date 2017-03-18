package com.github.oxaoo.qas;

import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.qas.training.QuestionModel;
import com.github.oxaoo.qas.training.SvmEngine;
import com.github.oxaoo.qas.training.TrainingUtils;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws FailedParsingException {
        parsing();
        training();
    }

    private static void training() {
        TrainingUtils utils = new TrainingUtils();
        List<QuestionModel> questionModels = utils.readTrainingSet("src/main/resources/qa/training.parse");
//        LOG.info("Training set: " + questionModels.toString());
//        QuestionModel tm = questionModels.get(0);
//        LOG.info("TM before sort:" + tm.toString());
//        tm.sortByHead();
//        LOG.info("TM after  sort:" + tm.toString());
        utils.readTrainingMap("src/main/resources/qa/training_map.txt", questionModels);
//        LOG.info("TM: " + questionModels.toString());
        LOG.info("TM:" + new GsonBuilder().setPrettyPrinting().create().toJson(questionModels));
        utils.makeModel("src/main/resources/qa/qa_model.txt", questionModels);

        List<QuestionModel> qms = utils.readQTrainingModel("src/main/resources/qa/qa_model.txt");

        SvmEngine svmEngine = new SvmEngine();
        int sizeQms = qms.size();
        svmEngine.run(qms.subList(0, sizeQms / 3 * 2), qms.subList(sizeQms / 3 * 2, sizeQms));
    }

    private static void parsing() throws FailedParsingException {
        String parserDir = "src/main/resources/parser/";
        String textFilePath = parserDir + "text.txt";
        String classifierModel = parserDir + "russian-utf8.par";
        String parserConfig = parserDir + "russian.mco";
        String resultParseFile = new RussianParser(classifierModel, parserDir, parserConfig).parseFromFile(textFilePath);
        LOG.info("Result of parse file: " + resultParseFile);
    }
}
