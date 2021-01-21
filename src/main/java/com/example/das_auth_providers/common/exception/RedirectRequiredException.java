package com.example.das_auth_providers.common.exception;

import org.springframework.security.access.AccessDeniedException;

public class RedirectRequiredException extends AccessDeniedException {
    private final String redirectUri;

    public RedirectRequiredException(final String redirectUri) {
        super("");
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
