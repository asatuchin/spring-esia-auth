package com.example.das_auth_providers.vk.service;

import com.example.das_auth_providers.vk.model.response.VKAccessToken;
import com.example.das_auth_providers.vk.model.response.VKProfile;
import com.example.das_auth_providers.vk.model.response.VKUsersResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// TODO: Implement VK requests logic.
@Service
public class VKService {

    private final RestTemplate restTemplate;

    @Value("${vk.api.authorization_uri:https://oauth.vk.com/authorize}")
    private URI vkAuthorizationURI;
    @Value("${vk.api.access_token_uri:https://oauth.vk.com/access_token}")
    private URI vkAccessTokenURI;
    @Value("${vk.api.security.app_id}")
    private String vkAppId;
    @Value("${vk.api.security.app_secret}")
    private String vkAppSecret;
    @Value("${vk.api.version}")
    private String vkApiVersion;

    public VKService(
            final RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public URI getAuthorizationURI(
            final List<String> permissions,
            final URI redirectUri
    ) {
        return UriComponentsBuilder.fromUri(vkAuthorizationURI)
                .queryParam("client_id", vkAppId)
                .queryParam("scope", String.join(",", permissions))
                .queryParam("redirect_uri", redirectUri.toString())
                .queryParam("response_type", "code")
                .queryParam("v", vkApiVersion)
                .build().toUri();
    }

    public VKAccessToken getAccessToken(final String code, final URI redirectUri) {
        URI accessTokenUri = UriComponentsBuilder.fromUri(vkAccessTokenURI)
                .queryParam("client_id", vkAppId)
                .queryParam("client_secret", vkAppSecret)
                .queryParam("code", code)
                .queryParam("redirect_uri", redirectUri.toString())
                .build().toUri();
        return restTemplate.getForEntity(accessTokenUri, VKAccessToken.class)
                .getBody();
    }

    public VKProfile getProfile(final VKAccessToken token) {
        URI profileUri = UriComponentsBuilder.fromUriString("https://api.vk.com/method/users.get")
                .queryParam("user_ids", token.getUserId())
                .queryParam("access_token", token.getAccessToken())
                .queryParam("v", vkApiVersion)
                .build().toUri();
        VKUsersResponse response = restTemplate.getForEntity(profileUri, VKUsersResponse.class)
                .getBody();
        if (response == null || CollectionUtils.isEmpty(response.getResponse())) {
            return null;
        }
        return response.getResponse().get(0);
    }
}
