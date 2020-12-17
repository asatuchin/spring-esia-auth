package com.example.spring_esia_auth.esia.service;

import com.example.spring_esia_auth.esia.model.EsiaOAuth2TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class EsiaApiService {

    private final RestOperations restOperations;
    private final EsiaRequestBuilderService requestBuilderService;

    public EsiaApiService(
            final RestOperations restOperations,
            final EsiaRequestBuilderService requestBuilderService
    ) {
        this.restOperations = restOperations;
        this.requestBuilderService = requestBuilderService;
    }

    public EsiaOAuth2TokenResponse getToken(final String authorizationCode) {
        return restOperations.postForEntity(
                requestBuilderService.getTokenUrl(authorizationCode),
                requestBuilderService.getTokenRequest(authorizationCode),
                EsiaOAuth2TokenResponse.class
        ).getBody();
    }
}
