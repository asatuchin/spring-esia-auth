package com.example.das_auth_providers.esia.security;

import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.esia.model.EsiaJwtPayload;
import com.example.das_auth_providers.esia.model.EsiaOAuth2TokenResponse;
import com.example.das_auth_providers.esia.model.EsiaSubjectData;
import com.example.das_auth_providers.esia.model.EsiaUserLink;
import com.example.das_auth_providers.esia.repository.EsiaUserLinkRepository;
import com.example.das_auth_providers.esia.service.EsiaApiService;
import com.example.das_auth_providers.esia.service.EsiaIdentityValidationService;
import com.example.das_auth_providers.das_emulation.exception.JwtParsingException;
import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Component
public class EsiaAuthenticationProvider implements AuthenticationProvider {

    private final EsiaApiService esiaApiService;
    private final EsiaIdentityValidationService esiaIdentityValidationService;
    private final EsiaUserLinkRepository esiaUserLinkRepository;
    private final UserRepository userRepository;
    private final String loginPageUri;
    private final URI registrationPageUri;

    public EsiaAuthenticationProvider(
            final EsiaApiService esiaApiService,
            final EsiaIdentityValidationService esiaIdentityValidationService,
            final EsiaUserLinkRepository esiaUserLinkRepository,
            final UserRepository userRepository,
            @Value("${esia.login.uri}") final String loginPageUri,
            @Value("${eios.registration.uri}") final URI registrationPageUri
    ) {
        this.esiaApiService = esiaApiService;
        this.esiaIdentityValidationService = esiaIdentityValidationService;
        this.esiaUserLinkRepository = esiaUserLinkRepository;
        this.userRepository = userRepository;
        this.loginPageUri = loginPageUri;
        this.registrationPageUri = registrationPageUri;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        }

        if (!(authentication.getPrincipal() instanceof EsiaAuthorizationCodeAuthToken)) {
            throw new BadCredentialsException("Authentication principal is not ESIA authorization code");
        }

        EsiaAuthorizationCodeAuthToken authorizationCode = (EsiaAuthorizationCodeAuthToken) authentication.getPrincipal();
        if (authorizationCode.getPrincipal() == null) {
            throw new RedirectRequiredException(loginPageUri);
        }

        EsiaOAuth2TokenResponse tokenResponse = esiaApiService.getToken((String) authorizationCode.getPrincipal());
        if (tokenResponse == null) {
            throw new AuthenticationServiceException("Failed fetching ESIA access token");
        }

        try {
            esiaIdentityValidationService.validateIdentityToken(tokenResponse);
        } catch (JwtValidationException e) {
            throw new AuthenticationServiceException("Failed ESIA token validation", e);
        }

        EsiaJwtPayload payload;
        try {
            payload = esiaIdentityValidationService.getPayload(tokenResponse.getIdToken());
        } catch (JwtParsingException e) {
            throw new AuthenticationServiceException("Failed ESIA token payload extraction");
        }

        Optional<EsiaUserLink> link = esiaUserLinkRepository.findByEsiaSubjectId(payload.getSbjId());
        if (link.isPresent()) {
            Optional<User> user = userRepository.findById(link.get().getUserId());
            if (user.isPresent()) {
                return new UsernamePasswordAuthenticationToken(user.get().getUsername(), user);
            }
            throw new AuthenticationServiceException("Cannot find user by ID: " + link.get().getUserId());
        } else {
            throw new RedirectRequiredException(getRegistrationPageUrl(tokenResponse));
        }
    }

    private String getRegistrationPageUrl(final EsiaOAuth2TokenResponse token) {
        EsiaSubjectData subjectData = esiaApiService.getProfile(token);
        return UriComponentsBuilder.fromUri(registrationPageUri)
                .queryParam("firstName", subjectData.getFirstName())
                .queryParam("lastName", subjectData.getLastName())
                .queryParam("birthDate", subjectData.getBirthDate())
                .queryParam("passport", subjectData.getPassport())
                .build().toUriString();
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == EsiaAuthorizationCodeAuthToken.class;
    }
}