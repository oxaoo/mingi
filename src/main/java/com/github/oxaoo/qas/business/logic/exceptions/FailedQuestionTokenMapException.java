package com.github.oxaoo.qas.business.logic.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
public class FailedQuestionTokenMapException extends Exception {

    public FailedQuestionTokenMapException() {
    }

    public FailedQuestionTokenMapException(String message) {
        super(message);
    }

    public FailedQuestionTokenMapException(String message, Throwable cause) {
        super(message, cause);
    }
}
