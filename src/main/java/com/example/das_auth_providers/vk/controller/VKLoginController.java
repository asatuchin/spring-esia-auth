package com.example.das_auth_providers.vk.controller;

import com.example.das_auth_providers.vk.service.VKRequestValidationService;
import com.example.das_auth_providers.vk.service.VKApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vk")
public class VKLoginController {

    private final static List<String> DEFAULT_VK_PERMISSIONS = Arrays.asList(
            "email", "profile"
    );

    private final VKApiService vkService;
    private final VKRequestValidationService vkRequestValidationService;
    private final URI callbackUri;

    public VKLoginController(
            final VKApiService vkService,
            final VKRequestValidationService vkRequestValidationService,
            final @Value("${hostname}/login") URI callbackUri
    ) {
        this.vkService = vkService;
        this.vkRequestValidationService = vkRequestValidationService;
        this.callbackUri = callbackUri;
    }

    @GetMapping("/login")
    public RedirectView auth() {
        String requestId = UUID.randomUUID().toString();
        String requestHash = vkRequestValidationService.getRequestHash(requestId);
        URI vkAuthURI = vkService.getAuthUri(
                DEFAULT_VK_PERMISSIONS,
                Arrays.asList(requestId, requestHash)
        );
        return new RedirectView(vkAuthURI.toString());
    }
}
