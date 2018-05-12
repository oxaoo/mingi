package com.github.oxaoo.mingi.core.question;

import com.github.oxaoo.mingi.exceptions.FailedQuestionTokenMapException;
import com.github.oxaoo.mingi.exceptions.LoadQuestionClassifierModelException;
import com.github.oxaoo.mingi.core.question.training.QuestionModel;
import com.github.oxaoo.mingi.core.question.training.QuestionToken;
import com.github.oxaoo.mingi.core.question.training.SvmEngine;
import com.github.oxaoo.mp4ru.exceptions.FailedConllMapException;
import com.github.oxaoo.mp4ru.exceptions.FailedParsingException;
import com.github.oxaoo.mp4ru.syntax.RussianParser;
import com.github.oxaoo.mp4ru.syntax.tagging.Conll;
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

    public QuestionClassifier(final RussianParser parser, final String qasHome)
            throws LoadQuestionClassifierModelException {
        this.parser = parser;
        this.svmEngine = new SvmEngine();
        this.model = new DefaultQuestionClassifierModelLoader(this.parser, this.svmEngine).load(qasHome);
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
