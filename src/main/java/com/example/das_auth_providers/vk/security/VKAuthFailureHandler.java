package com.example.das_auth_providers.vk.security;

import com.example.das_auth_providers.vk.exception.VKAuthException;
import com.example.das_auth_providers.vk.model.response.VKProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Component
public class VKAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final static RedirectStrategy REDIRECT_STRATEGY = new DefaultRedirectStrategy();

    private final URI registrationPageURI;

    public VKAuthFailureHandler(
            @Value("${eios_auth_vk.registration_page_uri}") final URI registrationPageURI
    ) {
        this.registrationPageURI = registrationPageURI;
    }

    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception
    ) throws IOException {
        VKAuthException vkAuthException = (VKAuthException) exception;
        VKProfile profile = vkAuthException.getProfile();
        String registrationUriString = getRegistrationURI(profile, vkAuthException.getRegistrationId());
        REDIRECT_STRATEGY.sendRedirect(request, response, registrationUriString);
    }

    private String getRegistrationURI(final VKProfile profile, final String registrationId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(registrationPageURI);
        if (profile == null) {
            return builder.toUriString();
        }
        return builder.queryParam("vk_id", profile.getId())
                .queryParam("first_name", profile.getFirstName())
                .queryParam("last_name", profile.getLastName())
                .queryParam("registration_id", registrationId)
                .build().encode().toUriString();
    }
}
