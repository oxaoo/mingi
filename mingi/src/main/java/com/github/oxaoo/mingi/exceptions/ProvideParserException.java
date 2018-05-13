package com.github.oxaoo.mingi.exceptions;

/**
 * @author Alexander Kuleshov
 * @version 1.0
 * @since 04.04.2017
 */
public class ProvideParserException extends Exception {

    public ProvideParserException() {
    }

    public ProvideParserException(String message) {
        super(message);
    }

    public ProvideParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
