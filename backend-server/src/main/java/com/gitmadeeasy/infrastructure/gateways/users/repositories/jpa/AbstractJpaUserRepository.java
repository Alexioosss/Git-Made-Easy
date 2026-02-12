package com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.users.JpaUserSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractJpaUserRepository extends JpaRepository<JpaUserSchema, Long> {
    Optional<UserSchema> findByEmailAddress(String emailAddress);
    boolean existsByEmailAddress(String emailAddress);
}