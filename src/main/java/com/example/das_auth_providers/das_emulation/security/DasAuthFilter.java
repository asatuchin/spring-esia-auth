package com.example.das_auth_providers.das_emulation.security;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Class is used to autowire all implementations of custom auth filters in the security configuration.
 */
public abstract class DasAuthFilter extends OncePerRequestFilter {
}
