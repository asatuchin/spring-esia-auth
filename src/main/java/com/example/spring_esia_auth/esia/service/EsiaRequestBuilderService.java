package com.example.spring_esia_auth.esia.service;

import com.example.spring_esia_auth.esia.model.EsiaOAuth2TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class EsiaRequestBuilderService {

    private final static String CLIENT_ID = "client_id";
    private final static String CLIENT_SECRET = "client_secret";
    private final static String REDIRECT_URI = "redirect_uri";
    private final static String SCOPE = "scope";
    private final static String RESPONSE_TYPE = "response_type";
    private final static String STATE = "state";
    private final static String TIMESTAMP = "timestamp";

    private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${esia.authorization.redirect_url}")
    private String redirectUrl;

    @Value("${esia.authorization.code_url}")
    private String authorizationCodeUrl;

    @Value("${esia.authorization.token_url}")
    private String tokenUrl;

    @Value("${esia.authorization.clientId}")
    private String clientId;

    @Value("${esia.authorization.scope}")
    private String scope;

    private final EsiaSignatureService signatureService;

    public EsiaRequestBuilderService(
            final EsiaSignatureService signatureService
    ) {
        this.signatureService = signatureService;
    }

    private String getAuthorizationUrl() {
        String timeStamp = this.getTimeStamp();
        String state = UUID.randomUUID().toString();
        String message = scope + timeStamp + clientId + state;

        String clientSecret = signatureService.sign(message);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {{
            add(CLIENT_ID, clientId);
            add(CLIENT_SECRET, clientSecret);
            add(REDIRECT_URI, redirectUrl);
            add(SCOPE, scope);
            add(RESPONSE_TYPE, "code");
            add(STATE, state);
            add(TIMESTAMP, timeStamp);
        }};
        return UriComponentsBuilder.fromUriString(authorizationCodeUrl)
                .queryParams(params)
                .toUriString();
    }

    public String getTokenUrl(final String code) {
        return tokenUrl;
    }


    public EsiaOAuth2TokenRequest getTokenRequest(final String code) {
        String timeStamp = this.getTimeStamp();
        String state = UUID.randomUUID().toString();
        String message = scope + timeStamp + clientId + state;

        String clientSecret = signatureService.sign(message);

        return EsiaOAuth2TokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType("authorization_code")
                .code(code)
                .redirectUri(redirectUrl)
                .scope(scope)
                .state(state)
                .timestamp(timeStamp)
                .build();
    }

    private String getTimeStamp() {
        return SQL_DATE_FORMAT.format(new Date());
    }
}
