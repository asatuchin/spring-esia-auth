package com.example.das_auth_providers.vk.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public class VKAuthCodeToken extends AbstractAuthenticationToken {
    private final String code;
    private final URI codeRedirectUri;

    public VKAuthCodeToken(final String code, final URI codeRedirectUri) {
        super(Collections.emptyList());
        this.code = code;
        this.codeRedirectUri = codeRedirectUri;
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

    public URI getRedirectUri() {
        return codeRedirectUri;
    }
}
