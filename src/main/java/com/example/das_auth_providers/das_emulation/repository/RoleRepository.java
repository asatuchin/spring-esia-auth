package com.example.das_auth_providers.das_emulation.repository;

import com.example.das_auth_providers.das_emulation.entity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
