package com.github.oxaoo.mingi.core.question.training;

import com.github.oxaoo.mingi.core.question.QuestionDomain;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public class QuestionModel {
    private int modelId;
    private QuestionDomain domain;
    private List<QuestionToken> questionTokens = new ArrayList<>();

    public QuestionModel() {
    }

    public QuestionModel(int modelId) {
        this.modelId = modelId;
    }

    public QuestionModel(QuestionDomain domain) {
        this.domain = domain;
    }

    public QuestionModel(QuestionDomain domain, List<QuestionToken> questionTokens) {
        this.domain = domain;
        this.questionTokens = questionTokens;
    }

    //todo check valueOf
    public QuestionModel(String domainValue, List<QuestionToken> questionTokens) {
        this.domain = QuestionDomain.valueOf(domainValue);
        this.questionTokens = questionTokens;
    }

    public QuestionModel(int modelId, QuestionDomain domain, List<QuestionToken> questionTokens) {
        this.modelId = modelId;
        this.domain = domain;
        this.questionTokens = questionTokens;
    }

    public QuestionDomain getDomain() {
        return domain;
    }

    public void setDomain(QuestionDomain domain) {
        this.domain = domain;
    }

    public List<QuestionToken> getQuestionTokens() {
        return questionTokens;
    }

    public void setQuestionTokens(List<QuestionToken> questionTokens) {
        this.questionTokens = questionTokens;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public void addQuestionToken(QuestionToken questionToken) {
        questionTokens.add(questionToken);
    }

    public void sortByHead() {
        questionTokens.sort(Comparator.comparingInt(QuestionToken::getHead));
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "modelId=" + modelId +
                ", domain=" + domain +
                ", questionTokens=" + questionTokens +
                '}';
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public String toSvmFormat() {
        StringBuilder sb = new StringBuilder(String.valueOf(this.domain.ordinal()));
        int i = 0;
        for (QuestionToken questionToken : this.questionTokens) {
            sb//.append(this.domain.ordinal())
              .append(" ")
//              .append(++i)
//              .append(":")
              .append(questionToken.getPos().getLabel());
        }
        return sb.toString();
    }
}
