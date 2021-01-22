package com.example.das_auth_providers.esia.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class EsiaAuthCodeToken extends AbstractAuthenticationToken {
    private final String code;

    public EsiaAuthCodeToken(final String code) {
        super(Collections.emptyList());
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public String getCode() {
        return code;
    }
}
