package com.example.das_auth_providers.esia.service;

import com.example.das_auth_providers.esia.exception.EsiaClientSecretException;
import com.example.das_auth_providers.esia.exception.EsiaSignatureException;
import com.example.das_auth_providers.esia.model.ESIAParameters;
import com.example.das_auth_providers.esia.model.api.EsiaOAuth2TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class EsiaRequestBuilderService {

    private static final SimpleDateFormat ESIA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String CODE_RESPONSE_TYPE_VAL = "code";

    private final EsiaSignatureService signatureService;

    private final URI authUri;
    private final URI authCallbackUri;
    private final URI accessTokenUri;

    private final String clientId;
    private final String scope;

    public EsiaRequestBuilderService(
            final EsiaSignatureService signatureService,
            final @Value("${esia.api.method_uri.auth_code}") URI authUri,
            final @Value("${security.auth.third-party.callback-uri}") URI authCallbackUri,
            final @Value("${esia.api.method_uri.access_token}") URI tokenUrl,
            final @Value("${esia.api.security.client_id}") String clientId,
            final @Value("${esia.api.security.scope}") String scope
    ) {
        this.signatureService = signatureService;
        this.authUri = authUri;
        this.authCallbackUri = authCallbackUri;
        this.accessTokenUri = tokenUrl;
        this.clientId = clientId;
        this.scope = scope;
    }

    public String getAuthorizationUrl() throws EsiaSignatureException {
        String timeStamp = this.getTimeStamp();
        String state = UUID.randomUUID().toString();
        String message = scope + timeStamp + clientId + state;

        String clientSecret = signatureService.sign(message);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {{
            add(ESIAParameters.CLIENT_ID, clientId);
            add(ESIAParameters.CLIENT_SECRET, clientSecret);
            add(ESIAParameters.REDIRECT_URI, authCallbackUri.toString());
            add(ESIAParameters.SCOPE, scope);
            add(ESIAParameters.RESPONSE_TYPE, CODE_RESPONSE_TYPE_VAL);
            add(ESIAParameters.STATE, state);
            add(ESIAParameters.TIMESTAMP, timeStamp);
        }};
        return UriComponentsBuilder.fromUri(authUri)
                .queryParams(params)
                .toUriString();
    }

    public URI getTokenUrl() {
        return accessTokenUri;
    }

    public EsiaOAuth2TokenRequest getTokenRequest(final String code) throws EsiaClientSecretException {
        String timeStamp = this.getTimeStamp();
        String state = UUID.randomUUID().toString();
        String message = scope + timeStamp + clientId + state;

        String clientSecret = null;
        try {
            clientSecret = signatureService.sign(message);
        } catch (EsiaSignatureException e) {
            throw new EsiaClientSecretException("Failed to create client secret", e);
        }

        return EsiaOAuth2TokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(ESIAParameters.AUTH_CODE)
                .code(code)
                .redirectUri(authCallbackUri.toString())
                .scope(scope)
                .state(state)
                .timestamp(timeStamp)
                .build();
    }

    private String getTimeStamp() {
        return ESIA_DATE_FORMAT.format(new Date());
    }
}
