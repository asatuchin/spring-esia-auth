package com.example.das_auth_providers.vk.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Map;

@Component
public class VKAuthRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            Map<String,String[]> params = request.getParameterMap();
            if (!params.isEmpty() && params.containsKey("auth_token")) {
                String token = params.get("auth_token")[0];
                if (token != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}