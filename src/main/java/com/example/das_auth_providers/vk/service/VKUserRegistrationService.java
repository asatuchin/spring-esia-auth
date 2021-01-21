package com.example.das_auth_providers.vk.service;

import com.example.das_auth_providers.common.exception.AccountLinkingException;
import com.example.das_auth_providers.common.service.ThirdPartyRegistrationService;
import com.example.das_auth_providers.das_emulation.entity.AuthProviders;
import com.example.das_auth_providers.das_emulation.repository.UserRepository;
import com.example.das_auth_providers.vk.entity.domain.VKUserLink;
import com.example.das_auth_providers.vk.repository.VKUserLinkRepository;
import org.springframework.stereotype.Service;

@Service
public class VKUserRegistrationService implements ThirdPartyRegistrationService {

    private final UserRepository userRepository;
    private final VKUserLinkRepository vkUserLinkRepository;

    public VKUserRegistrationService(
            final UserRepository userRepository,
            final VKUserLinkRepository vkUserLinkRepository
    ) {
        this.userRepository = userRepository;
        this.vkUserLinkRepository = vkUserLinkRepository;
    }

    public void createUserLink(final long localUserId, final String vkUserId) throws AccountLinkingException {
        VKUserLink link = vkUserLinkRepository.findByVkId(vkUserId);
        if (link != null) {
            if (link.getUserId() != localUserId) {
                throw new AccountLinkingException("VK account is linked to another user");
            }
            return;
        }
        userRepository.findById(localUserId)
                .map(
                    u -> vkUserLinkRepository.save(
                            VKUserLink.builder()
                                    .userId(localUserId)
                                    .vkId(vkUserId)
                                    .build()
                    )
                ).orElseThrow(() -> new AccountLinkingException("User does not exist"));
    }

    @Override
    public AuthProviders getProvider() {
        return AuthProviders.VK;
    }
}
