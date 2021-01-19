package com.example.das_auth_providers.das_emulation.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured_resource")
public class SecuredResourceController {

    @GetMapping("")
    public String getResource(SecurityContext securityContext) {
        return "Hello " + securityContext.getAuthentication().getPrincipal();
    }
}
