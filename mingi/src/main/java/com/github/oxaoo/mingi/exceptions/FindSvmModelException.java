package com.github.oxaoo.mingi.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 18.03.2017
 */
public class FindSvmModelException extends Exception {

    public FindSvmModelException() {
    }

    public FindSvmModelException(String message) {
        super(message);
    }

    public FindSvmModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
