package com.example.das_auth_providers.esia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EsiaOAuth2TokenRequest {
    private final static String CLIENT_ID = "client_id";
    private final static String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CODE = "code";
    private final static String REDIRECT_URI = "redirect_uri";
    private final static String SCOPE = "scope";
    private final static String RESPONSE_TYPE = "response_type";
    private final static String STATE = "state";
    private final static String TIMESTAMP = "timestamp";

    @JsonProperty(CLIENT_ID)
    String clientId;
    @JsonProperty(CLIENT_SECRET)
    String clientSecret;
    @JsonProperty(GRANT_TYPE)
    String grantType;
    @JsonProperty(CODE)
    String code;
    @JsonProperty(REDIRECT_URI)
    String redirectUri;
    @JsonProperty(SCOPE)
    String scope;
    @JsonProperty(STATE)
    String state;
    @JsonProperty(TIMESTAMP)
    String timestamp;
}
