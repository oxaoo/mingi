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
public class TrainingModel {
    private int modelId;
    private QuestionDomain domain;
    private List<ModelInfo> questionModel = new ArrayList<>();

    public TrainingModel() {
    }

    public TrainingModel(int modelId) {
        this.modelId = modelId;
    }

    public TrainingModel(QuestionDomain domain) {
        this.domain = domain;
    }

    public TrainingModel(QuestionDomain domain, List<ModelInfo> questionModel) {
        this.domain = domain;
        this.questionModel = questionModel;
    }

    //todo check valueOf
    public TrainingModel(String domainValue, List<ModelInfo> questionModel) {
        this.domain = QuestionDomain.valueOf(domainValue);
        this.questionModel = questionModel;
    }

    public TrainingModel(int modelId, QuestionDomain domain, List<ModelInfo> questionModel) {
        this.modelId = modelId;
        this.domain = domain;
        this.questionModel = questionModel;
    }

    public QuestionDomain getDomain() {
        return domain;
    }

    public void setDomain(QuestionDomain domain) {
        this.domain = domain;
    }

    public List<ModelInfo> getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(List<ModelInfo> questionModel) {
        this.questionModel = questionModel;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public void addModelInfo(ModelInfo modelInfo) {
        questionModel.add(modelInfo);
    }

    public void sortByHead() {
        questionModel.sort(Comparator.comparingInt(ModelInfo::getHead));
    }

    @Override
    public String toString() {
        return "TrainingModel{" +
                "modelId=" + modelId +
                ", domain=" + domain +
                ", questionModel=" + questionModel +
                '}';
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public String toSvmFormat() {
        StringBuilder sb = new StringBuilder(String.valueOf(this.domain.ordinal()));
        int i = 0;
        for (ModelInfo modelInfo : this.questionModel) {
            sb//.append(this.domain.ordinal())
              .append(" ")
              .append(++i)
              .append(":")
              .append(modelInfo.getPos().getLabel());
        }
        return sb.toString();
    }
}
