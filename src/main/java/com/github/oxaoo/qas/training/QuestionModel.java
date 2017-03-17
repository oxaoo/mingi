package com.github.oxaoo.qas.training;

import com.github.oxaoo.qas.qa.QuestionDomain;
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
    private List<ModelInfo> questionModelInfo = new ArrayList<>();

    public QuestionModel() {
    }

    public QuestionModel(int modelId) {
        this.modelId = modelId;
    }

    public QuestionModel(QuestionDomain domain) {
        this.domain = domain;
    }

    public QuestionModel(QuestionDomain domain, List<ModelInfo> questionModelInfo) {
        this.domain = domain;
        this.questionModelInfo = questionModelInfo;
    }

    //todo check valueOf
    public QuestionModel(String domainValue, List<ModelInfo> questionModelInfo) {
        this.domain = QuestionDomain.valueOf(domainValue);
        this.questionModelInfo = questionModelInfo;
    }

    public QuestionModel(int modelId, QuestionDomain domain, List<ModelInfo> questionModelInfo) {
        this.modelId = modelId;
        this.domain = domain;
        this.questionModelInfo = questionModelInfo;
    }

    public QuestionDomain getDomain() {
        return domain;
    }

    public void setDomain(QuestionDomain domain) {
        this.domain = domain;
    }

    public List<ModelInfo> getQuestionModelInfo() {
        return questionModelInfo;
    }

    public void setQuestionModelInfo(List<ModelInfo> questionModelInfo) {
        this.questionModelInfo = questionModelInfo;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public void addModelInfo(ModelInfo modelInfo) {
        questionModelInfo.add(modelInfo);
    }

    public void sortByHead() {
        questionModelInfo.sort(Comparator.comparingInt(ModelInfo::getHead));
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "modelId=" + modelId +
                ", domain=" + domain +
                ", questionModelInfo=" + questionModelInfo +
                '}';
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public String toSvmFormat() {
        StringBuilder sb = new StringBuilder(String.valueOf(this.domain.ordinal()));
        int i = 0;
        for (ModelInfo modelInfo : this.questionModelInfo) {
            sb//.append(this.domain.ordinal())
              .append(" ")
//              .append(++i)
//              .append(":")
              .append(modelInfo.getPos().getLabel());
        }
        return sb.toString();
    }
}
