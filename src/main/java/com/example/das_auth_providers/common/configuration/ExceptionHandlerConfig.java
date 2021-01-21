package com.example.das_auth_providers.common.configuration;

import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Configuration
public class ExceptionHandlerConfig {

    @ExceptionHandler(RedirectRequiredException.class)
    public String handleException(final RedirectRequiredException e) {
        return "redirect:" + e.getRedirectUri();
    }
}
