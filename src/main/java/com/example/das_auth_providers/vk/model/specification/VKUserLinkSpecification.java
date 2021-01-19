package com.example.das_auth_providers.vk.model.specification;

import com.example.das_auth_providers.vk.model.domain.VKUserLink;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class VKUserLinkSpecification implements Specification<VKUserLink> {
    private final VKUserLink filter;

    public VKUserLinkSpecification(final VKUserLink filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(
            final Root<VKUserLink> root,
            final CriteriaQuery<?> cq,
            final CriteriaBuilder cb
    ) {
        Predicate p = cb.disjunction();

        if (filter.getEmail() != null) {
            p.getExpressions().add(cb.like(root.get("email"), "%" + filter.getEmail() + "%"));
        }

        if (filter.getVkId() != null) {
            p.getExpressions().add(cb.like(root.get("vk_id"), "%" + filter.getVkId() + "%"));
        }

        return p;
    }
}
