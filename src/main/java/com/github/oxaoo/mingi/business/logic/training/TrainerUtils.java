package com.github.oxaoo.mingi.business.logic.training;

import com.github.oxaoo.mp4ru.common.ResourceResolver;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mingi.business.logic.exceptions.ErrorId;
import com.github.oxaoo.mingi.business.logic.qa.question.QuestionDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class TrainerUtils {
    private static final Logger LOG = LoggerFactory.getLogger(TrainerUtils.class);

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
                questionModel.addQuestionToken(new QuestionToken(tokenId, pos, head));
            }
            return questionModels;
        } catch (IOException e) {
            LOG.error(ErrorId.READ_TRAINING_MODEL_EXCEPTION.getDescription(e));
        }
        return Collections.emptyList();
    }

    public static void readDomainsMap(String fileName, List<QuestionModel> questionModels) {
        try (BufferedReader br = new BufferedReader(ResourceResolver.getResourceAsStreamReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] trainPair = line.split("\\s");
                if (trainPair.length != 2) continue;

                int id = Integer.valueOf(trainPair[0]);
                QuestionDomain domain = QuestionDomain.valueOf(trainPair[1]);
                questionModels.get(id - 1).setDomain(domain);
            }
        } catch (IOException e) {
            LOG.error(ErrorId.READ_TRAINING_MAP_MODEL_EXCEPTION.getDescription(e));
        }
    }

    public static void makeModel(String fileName, List<QuestionModel> questionModels) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (QuestionModel questionModel : questionModels) {
                bw.write(questionModel.toSvmFormat() + "\n");
            }
        } catch (IOException e) {
            LOG.error(ErrorId.MAKE_SVM_MODEL_EXCEPTION.getDescription(e));
        }
    }


    public static List<QuestionModel> readQTrainingModel(String fileName) {
        List<QuestionModel> questionModels = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int questionNumber = 0;
            QuestionModel questionModel = new QuestionModel(questionNumber);
            while ((line = br.readLine()) != null) {
                String[] qModelLine = line.split("\\s");
                QuestionDomain qd = QuestionDomain.values[Integer.valueOf(qModelLine[0])];
                List<QuestionToken> mis = new ArrayList<>();
                for (int i = 1; i < qModelLine.length; i++) {
                    mis.add(new QuestionToken(i - 1, Integer.valueOf(qModelLine[i]), -1));
                }
                questionModels.add(new QuestionModel(qd, mis));
            }
            return questionModels;
        } catch (IOException e) {
            LOG.error(ErrorId.READ_QUESTION_TRAINING_MODEL_EXCEPTION.getDescription(e));
        }
        return Collections.emptyList();
    }

    public static List<String> readQuestions(String questionsFilePath) throws ReadInputTextException {
        List<String> questions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(ResourceResolver.getResourceAsStreamReader(questionsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                questions.add(line);
            }
        } catch (IOException e) {
            throw new ReadInputTextException("Failed to read the text file \'" + questionsFilePath + "\'.", e);
        }
        return questions;
    }
}
