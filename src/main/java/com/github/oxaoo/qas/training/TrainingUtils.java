package com.github.oxaoo.qas.training;

import com.github.oxaoo.qas.exceptions.ErrorId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class TrainingUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TrainingUtils.class);

    public List<TrainingModel> readTrainingSet(String fileName) {
        List<TrainingModel> trainingModels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int questionNumber = 0;
            TrainingModel trainingModel = new TrainingModel(questionNumber);
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    trainingModels.add(trainingModel);
                    trainingModel = new TrainingModel(++questionNumber);
                    continue;
                }
                String[] conll = line.split("\\s");
                int tokenId = Integer.valueOf(conll[0]);
                String pos = conll[4];
                int head = Integer.valueOf(conll[6]);
                trainingModel.addModelInfo(new ModelInfo(tokenId, pos, head));
            }
            return trainingModels;
        } catch (IOException e) {
            LOG.error(ErrorId.READ_TRAINING_MODEL_EXCEPTION.getDescription());
        }
        return Collections.emptyList();
    }
}
