package com.example.das_auth_providers.das_emulation.repository;

import com.example.das_auth_providers.das_emulation.entity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
