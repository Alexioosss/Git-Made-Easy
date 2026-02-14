package com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.users.JpaUserSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository @Profile("test")
public interface JpaUserRepository extends JpaRepository<JpaUserSchema, String> {
    Optional<JpaUserSchema> findByEmailAddress(String emailAddress);
    boolean existsByEmailAddress(String emailAddress);
}