package com.example.das_auth_providers.esia.repository;

import com.example.das_auth_providers.esia.model.domain.EsiaUserLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface EsiaUserLinkRepository extends JpaRepository<EsiaUserLink, Long> {
    EsiaUserLink findByEsiaSubjectId(final String subjectId);
}