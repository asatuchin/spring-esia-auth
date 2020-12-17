package com.example.spring_esia_auth.exception;

public class JwtParsingException extends Exception {
    public JwtParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
