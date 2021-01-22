package com.example.das_auth_providers.esia.controller;

import com.example.das_auth_providers.esia.exception.EsiaSignatureException;
import com.example.das_auth_providers.esia.service.EsiaRequestBuilderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/esia")
public class EsiaLoginController {

    private final EsiaRequestBuilderService requestBuilderService;

    public EsiaLoginController(
            final EsiaRequestBuilderService requestBuilderService
    ) {
        this.requestBuilderService = requestBuilderService;
    }

    @GetMapping("/login")
    public String auth() {
        try {
            return "redirect:" + requestBuilderService.getAuthorizationUrl();
        } catch (EsiaSignatureException e) {
            return "redirect:/login?error=eisa_access_error";
        }
    }
}
