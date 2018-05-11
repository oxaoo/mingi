package com.github.oxaoo.mingi.business.logic.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 04.04.2017
 */
public class InitQasEngineException extends Exception {

    public InitQasEngineException() {
    }

    public InitQasEngineException(String message) {
        super(message);
    }

    public InitQasEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
