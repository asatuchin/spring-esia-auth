package com.example.das_auth_providers.common.service;

import com.example.das_auth_providers.common.exception.AccountLinkingException;
import com.example.das_auth_providers.das_emulation.entity.AuthProviders;

public interface ThirdPartyRegistrationService {
    void createUserLink(final long localUserId, final String vkUserId) throws AccountLinkingException;

    AuthProviders getProvider();
}