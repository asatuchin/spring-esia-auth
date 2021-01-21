package com.example.das_auth_providers.das_emulation.security;

import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DasAuthFailureEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException e
    ) throws IOException {
        if (e instanceof RedirectRequiredException) {
            response.sendRedirect(((RedirectRequiredException) e).getRedirectUri());
        }
    }
}
