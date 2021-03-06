package com.example.das_auth_providers.esia.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaOAuth2TokenResponse {
    @JsonProperty("id_token")
    String idToken;
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("expiresIn")
    long expiresIn;
    @JsonProperty("state")
    String state;
    @JsonProperty("token_type")
    String tokenType;
    @JsonProperty("refresh_token")
    String refreshToken;
}
