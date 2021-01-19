package com.example.das_auth_providers.vk.controller;

import com.example.das_auth_providers.vk.model.response.VKAccessToken;
import com.example.das_auth_providers.vk.service.VKService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/login/vk")
public class VKLoginController {

    private final VKService vkService;
    private final String callbackUri;

    public VKLoginController(
            final VKService vkService,
            @Value("${eios_auth_vk.host}/login/vk/callback")
            final String callbackUri
    ) {
        this.vkService = vkService;
        this.callbackUri = callbackUri;
    }

    @GetMapping("")
    public RedirectView auth(
            @RequestParam(value = "redirect_uri") final String redirectUri
    ) {
        URI tokenUri = UriComponentsBuilder.fromUriString(callbackUri)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .toUri();
        List<String> permissions = Collections.singletonList("email");
        URI vkAuthURI = vkService.getAuthorizationURI(permissions, tokenUri);
        return new RedirectView(vkAuthURI.toString());
    }

    @GetMapping("/callback")
    public RedirectView getVkToken(
            final @RequestParam(value = "code") String code,
            final @RequestParam(value = "redirect_uri") String redirectUri
    ) {
        URI tokenUri = UriComponentsBuilder.fromUriString(callbackUri)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .toUri();
        VKAccessToken token = vkService.getAccessToken(code, tokenUri);
        return new RedirectView(
                UriComponentsBuilder.fromUriString(redirectUri)
                        .queryParam("access_token", token.getAccessToken())
                        .queryParam("user_id", token.getUserId())
                        .queryParam("email", token.getEmail())
                        .queryParam("expires", token.getExpiresIn())
                        .toUriString()
        );
    }
}
