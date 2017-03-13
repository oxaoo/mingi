package com.github.oxaoo.qas.training;

import com.github.oxaoo.mp4ru.syntax.tagging.Conll;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class TrainingUtils {

    public List<TrainingModel> readTrainingSet(String fileName) {
        List<TrainingModel> trainingModels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            TrainingModel trainingModel = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    if (trainingModel != null) trainingModels.add(trainingModel);
                    int questionNumber = Integer.valueOf(line.substring(1));
                    trainingModel = new TrainingModel(questionNumber);
                    continue;
                }
                if (line.isEmpty()) continue;

                String[] conll = line.split("\\s");
                int tokenId = Integer.valueOf(conll[0]);
                String pos = conll[4];
                int head = Integer.valueOf(conll[6]);
                trainingModel.addModelInfo(new ModelInfo(tokenId, pos, head));
            }
            //added last training model
            trainingModels.add(trainingModel);
            return trainingModels;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
