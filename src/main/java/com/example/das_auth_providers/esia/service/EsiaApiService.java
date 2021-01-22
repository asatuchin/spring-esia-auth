package com.example.das_auth_providers.esia.service;

import com.example.das_auth_providers.esia.exception.EsiaClientSecretException;
import com.example.das_auth_providers.esia.exception.EsiaRequestTokenException;
import com.example.das_auth_providers.esia.model.api.EsiaOAuth2TokenResponse;
import com.example.das_auth_providers.esia.model.api.EsiaPersonContacts;
import com.example.das_auth_providers.esia.model.api.EsiaSubjectData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class EsiaApiService {

    private final RestOperations restOperations;
    private final EsiaRequestBuilderService requestBuilderService;

    private final URI esiaPersonsUri;

    public EsiaApiService(
            final RestOperations restOperations,
            final EsiaRequestBuilderService requestBuilderService,
            final @Value("${esia.api.method_uri.persons}") URI esiaPersonsUri
    ) {
        this.restOperations = restOperations;
        this.requestBuilderService = requestBuilderService;
        this.esiaPersonsUri = esiaPersonsUri;
    }

    public EsiaOAuth2TokenResponse getToken(final String authorizationCode) throws EsiaRequestTokenException {
        try {
            return restOperations.postForEntity(
                    requestBuilderService.getTokenUrl(),
                    requestBuilderService.getTokenRequest(authorizationCode),
                    EsiaOAuth2TokenResponse.class
            ).getBody();
        } catch (EsiaClientSecretException e) {
            throw new EsiaRequestTokenException("Failed to get ESIA access token", e);
        }
    }

    public EsiaSubjectData getProfile(final EsiaOAuth2TokenResponse token, final String subjectId) {
        URI uri = UriComponentsBuilder.fromUri(esiaPersonsUri)
                .pathSegment(subjectId)
                .build().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getIdToken())
                .build();

        return restOperations.exchange(request, EsiaSubjectData.class).getBody();
    }

    public List<EsiaPersonContacts> getUserContacts(final EsiaOAuth2TokenResponse token, final String subjectId) {
        URI uri = UriComponentsBuilder.fromUri(esiaPersonsUri)
                .pathSegment(subjectId)
                .pathSegment("ctts")
                .build().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getIdToken())
                .build();
        return restOperations.exchange(request, new ParameterizedTypeReference<List<EsiaPersonContacts>>() {
        }).getBody();
    }
}
