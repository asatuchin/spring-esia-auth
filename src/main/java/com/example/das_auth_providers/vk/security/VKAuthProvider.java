package com.example.das_auth_providers.vk.security;

import com.example.das_auth_providers.das_emulation.entity.domain.User;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import com.example.das_auth_providers.vk.exception.VKAuthException;
import com.example.das_auth_providers.vk.model.domain.VKUserLink;
import com.example.das_auth_providers.vk.model.response.VKAccessToken;
import com.example.das_auth_providers.vk.model.response.VKProfile;
import com.example.das_auth_providers.vk.model.specification.VKUserLinkSpecification;
import com.example.das_auth_providers.vk.repository.VKUserLinkRepository;
import com.example.das_auth_providers.vk.service.VKService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Slf4j
@Component
public class VKAuthProvider implements AuthenticationProvider {

    private final VKService vkService;
    private final UserRepository userRepository;
    private final VKUserLinkRepository vkUserRepository;

    public VKAuthProvider(
            final VKService vkService,
            final UserRepository userRepository,
            final VKUserLinkRepository vkUserRepository
    ) {
        this.vkService = vkService;
        this.userRepository = userRepository;
        this.vkUserRepository = vkUserRepository;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        if (!StringUtils.hasLength(principal) && !principal.startsWith("VK-eservice")) {
            return null;
        }

        VKAccessToken token = (VKAccessToken) authentication.getCredentials();
        VKProfile profile = vkService.getProfile(token);

        if (profile == null) {
            // TODO: Redirect the error page.
            return null;
        }

        VKUserLink link = vkUserRepository.findOne(
                new VKUserLinkSpecification(VKUserLink.builder().vkId(token.getUserId()).build())
        ).orElse(null);

        if (link == null) {
            // TODO: Save UUID with the expiration time to the repository.
            String registrationCode = UUID.randomUUID().toString();
            throw new VKAuthException(
                    "VK ID=" + profile.getId() + " is not found in local database",
                    profile,
                    registrationCode
            );
        }

        User user = userRepository.findByEmail(link.getEmail());
        if (null == user) {
            throw new UsernameNotFoundException("");
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == UsernamePasswordAuthenticationToken.class;
    }
}
