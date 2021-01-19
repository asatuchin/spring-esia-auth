package com.example.das_auth_providers.das_emulation.repository;

import com.example.das_auth_providers.das_emulation.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
