package com.example.spring_esia_auth.configuration;

import com.example.spring_esia_auth.exception.RedirectRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(RedirectRequiredException.class)
    public String handleException(final RedirectRequiredException e) {
        return "redirect:" + e.getRedirectUri();
    }
}
