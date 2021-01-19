package com.example.das_auth_providers.esia.repository;

import com.example.das_auth_providers.esia.model.EsiaUserLink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface EsiaUserLinkRepository extends CrudRepository<EsiaUserLink, Long> {
    Optional<EsiaUserLink> findByEsiaSubjectId(final String subjectId);
}