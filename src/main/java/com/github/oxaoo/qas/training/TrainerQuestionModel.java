package com.github.oxaoo.qas.training;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.exceptions.ReadInputTextException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mp4ru.syntax.utils.ParserUtils;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
public class TrainerQuestionModel {
    private static final Logger LOG = LoggerFactory.getLogger(TrainerQuestionModel.class);

    private final String sampleQuestionsPath;
    private final String domainsQuestionsPath;
    private final String qasModelPath;

    //properties for parser
    private final String classifierModelPath;
    private final String treeTaggerHome;
    private final String parserConfigPath;

    public TrainerQuestionModel(String sampleQuestionsPath, String domainsQuestionsPath, String qasModelPath,
                                String classifierModelPath, String treeTaggerHome, String parserConfigPath) {
        this.sampleQuestionsPath = sampleQuestionsPath;
        this.domainsQuestionsPath = domainsQuestionsPath;
        this.qasModelPath = qasModelPath;
        this.classifierModelPath = classifierModelPath;
        this.treeTaggerHome = treeTaggerHome;
        this.parserConfigPath = parserConfigPath;
    }

    public void train() throws ReadInputTextException,
            FailedParsingException,
            FailedConllMapException,
            FailedQuestionTokenMapException {

        String questions = ParserUtils.readText(this.sampleQuestionsPath);
        RussianParser parser = new RussianParser(this.classifierModelPath, this.treeTaggerHome, this.parserConfigPath);
        List<String> parsingQuestionsTokens = parser.parse(questions);
        List<QuestionToken> questionTokens = new ArrayList<>();
        for (String token : parsingQuestionsTokens) {
            Conll conll = Conll.map(token);
            questionTokens.add(QuestionToken.map(conll));
        }
        List<QuestionModel> questionModels = this.formModels(questionTokens);
        TrainingUtils.readTrainingMap(this.domainsQuestionsPath, questionModels);
        LOG.info("TM:" + new GsonBuilder().setPrettyPrinting().create().toJson(questionModels));

        TrainingUtils.makeModel(this.qasModelPath, questionModels);

        List<QuestionModel> qms = TrainingUtils.readQTrainingModel(this.qasModelPath);

        SvmEngine svmEngine = new SvmEngine();
        int sizeQms = qms.size();
        svmEngine.run(qms.subList(0, sizeQms / 3 * 2), qms.subList(sizeQms / 3 * 2, sizeQms));
    }

    private List<QuestionModel> formModels(List<QuestionToken> questionTokens) {
        List<QuestionModel> questionModels = new ArrayList<>();
        int prevTokenId = 0;
        int modelId = 0;
        QuestionModel questionModel = new QuestionModel(++modelId);
        for (QuestionToken token : questionTokens) {
            //if current token from next question
            if (token.getTokenId() <= prevTokenId) {
                questionModels.add(questionModel);
                questionModel = new QuestionModel(++modelId);
            }
            questionModel.addQuestionToken(token);
            prevTokenId = token.getTokenId();
        }
        //add last question
        if (!questionTokens.isEmpty()) {
            questionModels.add(questionModel);
        }
        return questionModels;
    }

}
