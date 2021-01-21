package com.example.das_auth_providers.vk.security;

import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import com.example.das_auth_providers.das_emulation.service.UserDetailsServiceImpl;
import com.example.das_auth_providers.vk.entity.domain.VKUserLink;
import com.example.das_auth_providers.vk.entity.response.VKAccessToken;
import com.example.das_auth_providers.vk.entity.response.VKProfile;
import com.example.das_auth_providers.vk.repository.VKUserLinkRepository;
import com.example.das_auth_providers.vk.service.VKApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class VKAuthProvider implements AuthenticationProvider {

    private final VKApiService vkService;
    private final UserDetailsService userDetailsService;
    private final VKUserLinkRepository vkUserRepository;

    public VKAuthProvider(
            final VKApiService vkService,
            final UserDetailsServiceImpl userDetailsService,
            final VKUserLinkRepository vkUserRepository
    ) {
        this.vkService = vkService;
        this.userDetailsService = userDetailsService;
        this.vkUserRepository = vkUserRepository;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        VKAuthCodeToken vkCode = (VKAuthCodeToken) authentication;

        VKAccessToken token = vkService.getAccessToken(vkCode.getCode());
        if (token == null) {
            return authentication;
        }

        VKUserLink link = vkUserRepository.findByVkId(token.getUserId());
        if (link == null) {
            VKProfile profile = vkService.getProfile(token);
            if (profile == null) {
                throw new BadCredentialsException("Failed to get VK auth profile");
            }
            String registrationCode = UUID.randomUUID().toString();
            throw new RedirectRequiredException("/registration?email=" + token.getEmail());
        }

        UserDetails user = userDetailsService.loadUserByUsername(link.getEmail());
        return new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == VKAuthCodeToken.class;
    }
}
