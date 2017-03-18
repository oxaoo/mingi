package com.github.oxaoo.qas;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.training.QuestionModel;
import com.github.oxaoo.qas.training.SvmEngine;
import com.github.oxaoo.qas.training.TrainerQuestionModel;
import com.github.oxaoo.qas.training.TrainingUtils;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(com.github.oxaoo.mp4ru.Main.class);

    public static void main(String[] args) throws FailedParsingException, ReadInputTextException, FailedQuestionTokenMapException, FailedConllMapException {
        newTrain();
    }

    private static void  newTrain()
            throws FailedParsingException, FailedConllMapException, ReadInputTextException, FailedQuestionTokenMapException {
        String resPath = "src/main/resources/";
        String sampleQuestionsPath = resPath + "qa/sample_questions.txt";
        String domainsQuestionsPath = resPath + "qa/domains_questions.txt";
        String qasModelPath = resPath + "qa/qa_model.txt";
        String classifierModelPath = resPath + "parser/russian-utf8.par";
        String treeTaggerPath = resPath + "parser/";
        String parserConfigPath = resPath + "parser/russian.mco";
        TrainerQuestionModel trainer = new TrainerQuestionModel(sampleQuestionsPath, domainsQuestionsPath, qasModelPath,
                classifierModelPath, treeTaggerPath, parserConfigPath);
        trainer.train();
    }
}
