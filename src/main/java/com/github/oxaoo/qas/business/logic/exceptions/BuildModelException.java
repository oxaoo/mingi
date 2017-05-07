package com.github.oxaoo.qas.business.logic.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 07.05.2017
 */
public class BuildModelException extends Exception {

    public BuildModelException() {
    }

    public BuildModelException(String message) {
        super(message);
    }

    public BuildModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
