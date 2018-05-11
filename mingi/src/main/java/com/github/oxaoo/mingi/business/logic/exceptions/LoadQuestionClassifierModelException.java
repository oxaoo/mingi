package com.github.oxaoo.mingi.business.logic.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
public class LoadQuestionClassifierModelException extends Exception {

    public LoadQuestionClassifierModelException() {
    }

    public LoadQuestionClassifierModelException(String message) {
        super(message);
    }

    public LoadQuestionClassifierModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
