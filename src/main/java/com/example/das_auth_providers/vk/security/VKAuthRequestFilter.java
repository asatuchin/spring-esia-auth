package com.example.das_auth_providers.vk.security;

import com.example.das_auth_providers.das_emulation.security.DasAuthFilter;
import com.example.das_auth_providers.vk.entity.VKApiParams;
import com.example.das_auth_providers.vk.service.VKRequestValidationService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static com.example.das_auth_providers.vk.entity.VKApiParams.AUTH_CODE;

@Component
public class VKAuthRequestFilter extends DasAuthFilter {

    private final VKRequestValidationService requestValidationService;

    public VKAuthRequestFilter(
            final VKRequestValidationService requestValidationService
    ) {
        this.requestValidationService = requestValidationService;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain
    ) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (!isAnonymousUserRequest(context)) {
            chain.doFilter(request, response);
            return;
        }

        Map<String, String[]> params = request.getParameterMap();
        String[] stateParam = params.get(VKApiParams.STATE);
        if (stateParam != null && stateParam.length != 0) {
            String[] state = StringUtils.split(stateParam[0], ":");
            if (state != null && state.length > 1) {
                String requestId = state[0];
                String requestHash = state[1];

                if (requestValidationService.isValid(requestId, requestHash)) {
                    String code = params.get(AUTH_CODE)[0];
                    if (code != null) {
                        URI codeRedirectUri = UriComponentsBuilder.fromUriString(request.getRequestURI())
                                .build()
                                .toUri();
                        Authentication auth = new VKAuthCodeToken(code, codeRedirectUri);
                        context.setAuthentication(auth);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAnonymousUserRequest(final SecurityContext context) {
        Authentication auth = context.getAuthentication();
        return auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated();
    }

    private boolean matchesAuthorizationResponse(HttpServletRequest request) {
        if (!isAuthorizationResponse(request)) {
            return false;
        } else {
//            OAuth2AuthorizationRequest authorizationRequest = this.authorizationRequestRepository.loadAuthorizationRequest(request);
//            if (authorizationRequest == null) {
//                return false;
//            } else {
//                UriComponents requestUri = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request)).build();
//                UriComponents redirectUri = UriComponentsBuilder.fromUriString(authorizationRequest.getRedirectUri()).build();
//                Set<Map.Entry<String, List<String>>> requestUriParameters = new LinkedHashSet(requestUri.getQueryParams().entrySet());
//                Set<Map.Entry<String, List<String>>> redirectUriParameters = new LinkedHashSet(redirectUri.getQueryParams().entrySet());
//                requestUriParameters.retainAll(redirectUriParameters);
//                return Objects.equals(requestUri.getScheme(), redirectUri.getScheme()) && Objects.equals(requestUri.getUserInfo(), redirectUri.getUserInfo()) && Objects.equals(requestUri.getHost(), redirectUri.getHost()) && Objects.equals(requestUri.getPort(), redirectUri.getPort()) && Objects.equals(requestUri.getPath(), redirectUri.getPath()) && Objects.equals(requestUriParameters.toString(), redirectUriParameters.toString());
//            }
            return true;
        }
    }

    private boolean isAuthorizationResponse(HttpServletRequest request) {
        return true;
    }

    private void processAuthorizationResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        //
//        this.redirectStrategy.sendRedirect(request, response, "https://localhost:8080");
    }
}