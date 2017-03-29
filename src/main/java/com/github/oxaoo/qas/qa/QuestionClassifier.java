package com.github.oxaoo.qas.qa;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.qas.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.qas.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.qas.parse.ParserManager;
import com.github.oxaoo.qas.training.QuestionModel;
import com.github.oxaoo.qas.training.QuestionToken;
import com.github.oxaoo.qas.training.SvmEngine;
import libsvm.svm_model;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
public class QuestionClassifier {
    private final svm_model model;
    private final SvmEngine svmEngine;
    private final RussianParser parser;

    public QuestionClassifier() throws LoadQuestionClassifierModelException {
        this.parser = ParserManager.getParser();
        this.model = QuestionClassifierModelLoader.load();
        this.svmEngine = new SvmEngine();
    }

    public QuestionDomain classify(String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        List<String> parsedTokens = this.parser.parse(question);
        return this.classify(parsedTokens);
    }

    public QuestionDomain classify(List<String> questionTokens)
            throws FailedConllMapException, FailedQuestionTokenMapException {
        QuestionModel questionModel = new QuestionModel();
        for (String token : questionTokens) {
            Conll conll = Conll.map(token);
            QuestionToken questionToken = QuestionToken.map(conll);
            questionModel.addQuestionToken(questionToken);
        }
        return this.svmEngine.svmClassify(this.model, questionModel);
    }
}
