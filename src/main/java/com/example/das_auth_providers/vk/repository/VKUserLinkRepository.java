package com.example.das_auth_providers.vk.repository;

import com.example.das_auth_providers.vk.model.domain.VKUserLink;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VKUserLinkRepository extends PagingAndSortingRepository<VKUserLink, Long>,
        JpaSpecificationExecutor<VKUserLink> {
}
