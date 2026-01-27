package com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractJpaUserRepository extends JpaRepository<UserSchema, Long> {
    Optional<UserSchema> findByEmailAddress(String emailAddress);
}