package com.example.das_auth_providers.vk.service;

import com.example.das_auth_providers.vk.entity.VKApiParams;
import com.example.das_auth_providers.vk.entity.response.VKAccessToken;
import com.example.das_auth_providers.vk.entity.response.VKProfile;
import com.example.das_auth_providers.vk.entity.response.VKUsersResponse;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class VKApiService {

    private static final String CODE_RESPONSE_TYPE_VAL = "code";

    private final RestTemplate restTemplate;

    @Value("${security.auth.third-party.callback-uri}")
    private URI authCallbackUri;

    @Value("${vk.api.method_uri.authorization}")
    private URI vkAuthorizationURI;
    @Value("${vk.api.method_uri.access_token}")
    private URI vkAccessTokenURI;
    @Value("${vk.api.method_uri.get_users}")
    private URI usersUri;

    @Value("${vk.api.security.app_id}")
    private String vkAppId;
    @Value("${vk.api.security.app_secret}")
    private String vkAppSecret;
    @Value("${vk.api.version}")
    private String vkApiVersion;


    public VKApiService(
            final RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public URI getAuthUri(
            final List<String> permissions,
            final List<String> state
    ) {
        return UriComponentsBuilder.fromUri(vkAuthorizationURI)
                .queryParam(VKApiParams.CLIENT_ID, vkAppId)
                .queryParam(VKApiParams.SCOPE, String.join(",", permissions))
                .queryParam(VKApiParams.REDIRECT_URI, buildCallbackUri("/"))
                .queryParam(VKApiParams.STATE, StringUtils.join(state, ':'))
                .queryParam(VKApiParams.RESPONSE_TYPE, CODE_RESPONSE_TYPE_VAL)
                .queryParam(VKApiParams.VERSION, vkApiVersion)
                .build().toUri();
    }

    public VKAccessToken getAccessToken(final String code) {
        URI accessTokenUri = UriComponentsBuilder.fromUri(vkAccessTokenURI)
                .queryParam(VKApiParams.CLIENT_ID, vkAppId)
                .queryParam(VKApiParams.CLIENT_SECRET, vkAppSecret)
                .queryParam(VKApiParams.AUTH_CODE, code)
                .queryParam(VKApiParams.REDIRECT_URI, buildCallbackUri("/"))
                        .build().toUri();
        try {
            return restTemplate.getForEntity(accessTokenUri, VKAccessToken.class).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public VKProfile getProfile(final VKAccessToken token) {
        URI profileUri = UriComponentsBuilder.fromUri(usersUri)
                .queryParam(VKApiParams.USER_IDS, token.getUserId())
                .queryParam(VKApiParams.ACCESS_TOKEN, token.getAccessToken())
                .queryParam(VKApiParams.VERSION, vkApiVersion)
                .build().toUri();
        VKUsersResponse response = restTemplate.getForEntity(profileUri, VKUsersResponse.class)
                .getBody();
        if (response == null || CollectionUtils.isEmpty(response.getResponse())) {
            return null;
        }
        return response.getResponse().get(0);
    }

    private String buildCallbackUri(final String redirectUri) {
        return UriComponentsBuilder.fromUri(authCallbackUri)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }
}
