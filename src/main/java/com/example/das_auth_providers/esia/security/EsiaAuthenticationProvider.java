package com.example.das_auth_providers.esia.security;

import com.example.das_auth_providers.das_emulation.entity.AuthProviders;
import com.example.das_auth_providers.das_emulation.entity.RegistrationParameters;
import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.service.UserDetailsServiceImpl;
import com.example.das_auth_providers.esia.exception.EsiaRequestTokenException;
import com.example.das_auth_providers.esia.model.api.EsiaPersonContacts;
import com.example.das_auth_providers.esia.model.api.jwt.EsiaJwtPayload;
import com.example.das_auth_providers.esia.model.api.EsiaOAuth2TokenResponse;
import com.example.das_auth_providers.esia.model.api.EsiaSubjectData;
import com.example.das_auth_providers.esia.model.domain.EsiaUserLink;
import com.example.das_auth_providers.esia.repository.EsiaUserLinkRepository;
import com.example.das_auth_providers.esia.service.EsiaApiService;
import com.example.das_auth_providers.esia.service.EsiaIdentityValidationService;
import com.example.das_auth_providers.das_emulation.exception.JwtParsingException;
import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
public class EsiaAuthenticationProvider implements AuthenticationProvider {

    private final EsiaApiService esiaApiService;
    private final EsiaIdentityValidationService esiaIdentityValidationService;
    private final EsiaUserLinkRepository esiaUserLinkRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final URI registrationPageUri;

    public EsiaAuthenticationProvider(
            final EsiaApiService esiaApiService,
            final EsiaIdentityValidationService esiaIdentityValidationService,
            final EsiaUserLinkRepository esiaUserLinkRepository,
            final UserRepository userRepository,
            final UserDetailsServiceImpl userDetailsService,
            final @Value("${security.auth.registration-uri}") URI registrationPageUri
    ) {
        this.esiaApiService = esiaApiService;
        this.esiaIdentityValidationService = esiaIdentityValidationService;
        this.esiaUserLinkRepository = esiaUserLinkRepository;
        this.userRepository = userRepository;
        this.registrationPageUri = registrationPageUri;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        EsiaAuthCodeToken authCode = (EsiaAuthCodeToken) authentication;

        EsiaOAuth2TokenResponse tokenResponse = null;
        try {
            tokenResponse = esiaApiService.getToken(authCode.getCode());
        } catch (EsiaRequestTokenException e) {
            throw new BadCredentialsException("Failed fetching ESIA access token");
        }

        try {
            esiaIdentityValidationService.validateIdentityToken(tokenResponse);
        } catch (JwtValidationException e) {
            throw new BadCredentialsException("Failed ESIA token validation", e);
        }

        EsiaJwtPayload payload;
        try {
            payload = esiaIdentityValidationService.getPayload(tokenResponse.getIdToken());
        } catch (JwtParsingException e) {
            throw new BadCredentialsException("Failed ESIA token payload extraction");
        }

        EsiaUserLink link = esiaUserLinkRepository.findByEsiaSubjectId(payload.getSbjId());
        if (link == null) {
            throw new RedirectRequiredException(
                    getRegistrationPageUrl(tokenResponse, payload.getSbjId())
            );
        }
        User user = userRepository.findById(link.getUserId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    private String getRegistrationPageUrl(
            final EsiaOAuth2TokenResponse token,
            final String esiaSubjectId
    ) {
        EsiaSubjectData subjectData = esiaApiService.getProfile(token, esiaSubjectId);

        String email = esiaApiService.getUserContacts(token, esiaSubjectId).stream()
                .filter(c -> EsiaPersonContacts.Type.EMAIL.equals(c.getContactType()))
                .findFirst()
                .orElse(new EsiaPersonContacts())
                .getValue();

        return UriComponentsBuilder.fromUri(registrationPageUri)
                .queryParam(RegistrationParameters.EMAIL, email)
                .queryParam(RegistrationParameters.FIRST_NAME, subjectData.getFirstName())
                .queryParam(RegistrationParameters.LAST_NAME, subjectData.getLastName())
                .queryParam(RegistrationParameters.THIRD_PARTY_PROVIDER, AuthProviders.ESIA)
                .queryParam(RegistrationParameters.THIRD_PARTY_ID, esiaSubjectId)
                .build().toUriString();
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == EsiaAuthCodeToken.class;
    }
}