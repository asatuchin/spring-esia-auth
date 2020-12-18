package com.example.spring_esia_auth.esia.auth;

import lombok.Builder;
import lombok.Data;
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
