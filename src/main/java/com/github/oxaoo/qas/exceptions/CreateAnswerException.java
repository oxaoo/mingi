package com.github.oxaoo.qas.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 04.04.2017
 */
public class CreateAnswerException extends Exception {

    public CreateAnswerException() {
    }

    public CreateAnswerException(String message) {
        super(message);
    }

    public CreateAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}
