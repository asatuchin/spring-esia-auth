package com.example.das_auth_providers.esia.service;

import com.example.das_auth_providers.esia.model.EsiaOAuth2TokenResponse;
import com.example.das_auth_providers.esia.model.EsiaSubjectData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.net.URI;

@Service
public class EsiaApiService {

    private final RestOperations restOperations;
    private final EsiaRequestBuilderService requestBuilderService;
    private final URI esiaUserProfileUri;

    public EsiaApiService(
            final RestOperations restOperations,
            final EsiaRequestBuilderService requestBuilderService,
            @Value("${esia.api.profile}") final URI esiaUserProfileUri
    ) {
        this.restOperations = restOperations;
        this.requestBuilderService = requestBuilderService;
        this.esiaUserProfileUri = esiaUserProfileUri;
    }

    public EsiaOAuth2TokenResponse getToken(final String authorizationCode) {
        return restOperations.postForEntity(
                requestBuilderService.getTokenUrl(authorizationCode),
                requestBuilderService.getTokenRequest(authorizationCode),
                EsiaOAuth2TokenResponse.class
        ).getBody();
    }

    public EsiaSubjectData getProfile(final EsiaOAuth2TokenResponse token) {
        RequestEntity<Void> request = RequestEntity.get(esiaUserProfileUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getIdToken())
                .build();

        return restOperations.exchange(request, EsiaSubjectData.class).getBody();
    }
}
