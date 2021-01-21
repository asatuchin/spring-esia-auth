package com.example.das_auth_providers.common.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class RedirectRequiredException extends BadCredentialsException {
    private final String redirectUri;

    public RedirectRequiredException(final String redirectUri) {
        super("");
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
