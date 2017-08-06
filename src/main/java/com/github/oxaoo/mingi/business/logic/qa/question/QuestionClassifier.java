package com.github.oxaoo.mingi.business.logic.qa.question;

import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
import com.github.oxaoo.mingi.business.logic.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.business.logic.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.business.logic.exceptions.ProvideParserException;
import com.github.oxaoo.mingi.business.logic.parse.ParserManager;
import com.github.oxaoo.mingi.business.logic.training.QuestionModel;
import com.github.oxaoo.mingi.business.logic.training.QuestionToken;
import com.github.oxaoo.mingi.business.logic.training.SvmEngine;
import libsvm.svm_model;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 19.03.2017
 */
@Getter
@AllArgsConstructor
public class QuestionClassifier {
    private final svm_model model;
    private final SvmEngine svmEngine;
    private final RussianParser parser;

    public QuestionClassifier() throws LoadQuestionClassifierModelException, ProvideParserException {
        this.parser = ParserManager.getParser();
        this.svmEngine = new SvmEngine();
        this.model = new DefaultQuestionClassifierModelLoader(this.svmEngine).load();
    }

    public QuestionClassifier(RussianParser parser) throws LoadQuestionClassifierModelException {
        this.parser = parser;
        this.svmEngine = new SvmEngine();
        this.model = new DefaultQuestionClassifierModelLoader(this.svmEngine).load();
    }

    public QuestionClassifier(QuestionClassifierModelLoader modelLoader)
            throws LoadQuestionClassifierModelException, ProvideParserException {
        this.parser = ParserManager.getParser();
        this.model = modelLoader.load();
        this.svmEngine = new SvmEngine();
    }

    public QuestionClassifier(QuestionClassifierModelLoader modelLoader, RussianParser parser)
            throws LoadQuestionClassifierModelException {
        this.parser = parser;
        this.model = modelLoader.load();
        this.svmEngine = new SvmEngine();
    }

    public QuestionDomain classify(String question)
            throws FailedParsingException, FailedConllMapException, FailedQuestionTokenMapException {
        List<Conll> parsedTokens = this.parser.parse(question, Conll.class);
        return this.classify(parsedTokens);
    }

    public QuestionDomain classify(List<Conll> conllTokens)
            throws FailedConllMapException, FailedQuestionTokenMapException {
        QuestionModel questionModel = new QuestionModel();
        for (Conll conll : conllTokens) {
            QuestionToken questionToken = QuestionToken.map(conll);
            questionModel.addQuestionToken(questionToken);
        }
        return this.svmEngine.svmClassify(this.model, questionModel);
    }
}
