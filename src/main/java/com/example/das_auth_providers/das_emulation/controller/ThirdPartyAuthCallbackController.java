package com.example.das_auth_providers.das_emulation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth/third_party")
public class ThirdPartyAuthCallbackController {

    @GetMapping("/callback")
    public String redirectAuthenticated(
            final @RequestParam("redirect_uri") String redirectUri
    ) {
        return "redirect:" + redirectUri;
    }
}
