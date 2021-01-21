package com.example.das_auth_providers.vk.security;

import com.example.das_auth_providers.common.exception.RedirectRequiredException;
import com.example.das_auth_providers.das_emulation.entity.AuthProviders;
import com.example.das_auth_providers.das_emulation.entity.RegistrationParameters;
import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import com.example.das_auth_providers.das_emulation.service.UserDetailsServiceImpl;
import com.example.das_auth_providers.das_emulation.service.UserService;
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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class VKAuthProvider implements AuthenticationProvider {

    private final VKApiService vkService;
    private final UserDetailsService userDetailsService;
    private final VKUserLinkRepository vkUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public VKAuthProvider(
            final VKApiService vkService,
            final UserDetailsServiceImpl userDetailsService,
            final VKUserLinkRepository vkUserRepository,
            final UserRepository userRepository,
            final UserService userService
    ) {
        this.vkService = vkService;
        this.userDetailsService = userDetailsService;
        this.vkUserRepository = vkUserRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
            Map<String, String> parameters = new HashMap<String, String>() {{
                put(RegistrationParameters.EMAIL, token.getEmail());
                put(RegistrationParameters.FIRST_NAME, profile.getFirstName());
                put(RegistrationParameters.LAST_NAME, profile.getLastName());
            }};
            URI registrationUri = userService.getThirdPartyRegistrationLink(
                    AuthProviders.VK,
                    profile.getId(),
                    parameters
            );
            throw new RedirectRequiredException(registrationUri.toString());
        }
        
        User user = userRepository.findById(link.getUserId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == VKAuthCodeToken.class;
    }
}
