package com.github.oxaoo.mingi.business.logic.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
public class SaveSvmModelException extends Exception {

    public SaveSvmModelException() {
    }

    public SaveSvmModelException(String message) {
        super(message);
    }

    public SaveSvmModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
