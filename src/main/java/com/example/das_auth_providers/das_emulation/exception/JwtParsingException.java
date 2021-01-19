package com.example.das_auth_providers.das_emulation.exception;

public class JwtParsingException extends Exception {
    public JwtParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
