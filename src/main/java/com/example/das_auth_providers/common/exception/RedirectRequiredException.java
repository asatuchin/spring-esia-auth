package com.example.das_auth_providers.common.exception;

public class RedirectRequiredException extends RuntimeException {
    private final String redirectUri;

    public RedirectRequiredException(final String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
