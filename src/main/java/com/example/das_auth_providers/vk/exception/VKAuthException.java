package com.example.das_auth_providers.vk.exception;

import com.example.das_auth_providers.vk.entity.response.VKProfile;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class VKAuthException extends AuthenticationException {

    @Getter
    private final VKProfile profile;
    @Getter
    private final String registrationId;

    public VKAuthException(
            final String msg,
            final VKProfile profile,
            final String registrationId
    ) {
        super(msg);
        this.profile = profile;
        this.registrationId = registrationId;
    }
}
