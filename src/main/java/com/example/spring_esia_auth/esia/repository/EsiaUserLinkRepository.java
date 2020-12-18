package com.example.spring_esia_auth.esia.repository;

import com.example.spring_esia_auth.esia.model.EsiaUserLink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface EsiaUserLinkRepository extends CrudRepository<EsiaUserLink, Long> {
    Optional<EsiaUserLink> findByEsiaSubjectId(final String subjectId);
}