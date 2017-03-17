package com.github.oxaoo.qas.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 14.03.2017
 */
public enum ErrorId {
    READ_TRAINING_MODEL_EXCEPTION(100, "Error while read training model for question classifier"),
    READ_TRAINING_MAP_MODEL_EXCEPTION(110, "Error while read training map model for question classifier"),
    MAKE_SVM_MODEL_EXCEPTION(120, "Error while write SVM model for question classifier"),
    READ_QUESTION_TRAINING_MODEL_EXCEPTION(130, "Error while read question training model for question classifier");

    private int id;
    private String cause;

    ErrorId(int id, String cause) {
        this.id = id;
        this.cause = cause;
    }

    public String getDescription() {
        return this.id + ": " + this.cause;
    }

    public String getDescription(Throwable e) {
        return this.id + ": " + this.cause + "[" + e.getMessage() + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
