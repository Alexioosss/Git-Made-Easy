package com.gitmadeeasy.infrastructure.gateways.users.repositories;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;

import java.util.Optional;

public interface UserRepository {
    UserSchema save(UserSchema userSchema);
    Optional<UserSchema> findById(String userId);
    Optional<UserSchema> findByEmail(String emailAddress);
    boolean existsByEmail(String emailAddress);
    void deleteAll();
}