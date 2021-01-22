package com.example.das_auth_providers.esia.security;

import com.example.das_auth_providers.das_emulation.security.DasAuthFilter;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Component
public class EisaAuthorizationCodeGrantFilter extends DasAuthFilter {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

    public EisaAuthorizationCodeGrantFilter() {
        this.authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        if (this.matchesAuthorizationResponse(request)) {
            this.processAuthorizationResponse(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean matchesAuthorizationResponse(HttpServletRequest request) {
        if (!isAuthorizationResponse(request)) {
            return false;
        } else {
            OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository.loadAuthorizationRequest(request);
            if (authorizationRequest == null) {
                return false;
            } else {
                UriComponents requestUri = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request)).build();
                UriComponents redirectUri = UriComponentsBuilder.fromUriString(authorizationRequest.getRedirectUri()).build();
                Set<Map.Entry<String, List<String>>> requestUriParameters = new LinkedHashSet(requestUri.getQueryParams().entrySet());
                Set<Map.Entry<String, List<String>>> redirectUriParameters = new LinkedHashSet(redirectUri.getQueryParams().entrySet());
                requestUriParameters.retainAll(redirectUriParameters);
                return Objects.equals(requestUri.getScheme(), redirectUri.getScheme()) && Objects.equals(requestUri.getUserInfo(), redirectUri.getUserInfo()) && Objects.equals(requestUri.getHost(), redirectUri.getHost()) && Objects.equals(requestUri.getPort(), redirectUri.getPort()) && Objects.equals(requestUri.getPath(), redirectUri.getPath()) && Objects.equals(requestUriParameters.toString(), redirectUriParameters.toString());
            }
        }
    }

    private boolean isAuthorizationResponse(HttpServletRequest request) {
        return true;
    }

    private void processAuthorizationResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //
        this.redirectStrategy.sendRedirect(request, response, "https://localhost:8080");
    }
}
