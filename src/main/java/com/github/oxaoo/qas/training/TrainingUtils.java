package com.github.oxaoo.qas.training;

import com.github.oxaoo.qas.exceptions.ErrorId;
import com.github.oxaoo.qas.qa.QuestionDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class TrainingUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TrainingUtils.class);

    public List<QuestionModel> readTrainingSet(String fileName) {
        List<QuestionModel> questionModels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int questionNumber = 0;
            QuestionModel questionModel = new QuestionModel(questionNumber);
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    questionModels.add(questionModel);
                    questionModel = new QuestionModel(++questionNumber);
                    continue;
                }
                String[] conll = line.split("\\s");
                int tokenId = Integer.valueOf(conll[0]);
                String pos = conll[4];
                int head = Integer.valueOf(conll[6]);
                questionModel.addModelInfo(new ModelInfo(tokenId, pos, head));
            }
            return questionModels;
        } catch (IOException e) {
            LOG.error(ErrorId.READ_TRAINING_MODEL_EXCEPTION.getDescription(e));
        }
        return Collections.emptyList();
    }

    public void readTrainingMap(String fileName, List<QuestionModel> questionModels) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] trainPair = line.split("\\s");
                if (trainPair.length != 2) continue;

                int id = Integer.valueOf(trainPair[0]);
                QuestionDomain domain = QuestionDomain.valueOf(trainPair[1]);
                questionModels.get(id).setDomain(domain);
            }
        } catch (IOException e) {
            LOG.error(ErrorId.READ_TRAINING_MAP_MODEL_EXCEPTION.getDescription(e));
        }
    }

    public void makeModel(String fileName, List<QuestionModel> questionModels) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (QuestionModel questionModel : questionModels) {
                bw.write(questionModel.toSvmFormat() + "\n");
            }
        } catch (IOException e) {
            LOG.error(ErrorId.MAKE_SVM_MODEL_EXCEPTION.getDescription(e));
        }
    }

}
