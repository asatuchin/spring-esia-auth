package com.example.das_auth_providers.esia.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class EsiaAuthorizationCodeAuthToken extends AbstractAuthenticationToken {
    private final String code;

    public EsiaAuthorizationCodeAuthToken(String code, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.code = code;
    }

    public EsiaAuthorizationCodeAuthToken(String code) {
        super(Collections.emptyList());
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return code;
    }
}
