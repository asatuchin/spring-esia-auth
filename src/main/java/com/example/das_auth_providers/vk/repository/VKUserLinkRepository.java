package com.example.das_auth_providers.vk.repository;

import com.example.das_auth_providers.vk.entity.domain.VKUserLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface VKUserLinkRepository extends JpaRepository<VKUserLink, Long>,
        JpaSpecificationExecutor<VKUserLink> {
    VKUserLink findByVkId(final String vkId);
}
