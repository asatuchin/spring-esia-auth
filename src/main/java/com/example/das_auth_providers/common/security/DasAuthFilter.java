package com.example.das_auth_providers.common.security;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Class is used to autowire all implementations of custom auth filters in the security configuration.
 */
public abstract class DasAuthFilter extends OncePerRequestFilter {
}
