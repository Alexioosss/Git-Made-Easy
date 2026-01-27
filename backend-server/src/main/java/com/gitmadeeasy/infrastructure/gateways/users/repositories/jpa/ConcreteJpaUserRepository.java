package com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;

import java.util.Optional;

public class ConcreteJpaUserRepository implements UserRepository {
    private final AbstractJpaUserRepository jpa;

    public ConcreteJpaUserRepository(AbstractJpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public UserSchema save(UserSchema userSchema) {
        return this.jpa.save(userSchema);
    }

    @Override
    public Optional<UserSchema> findById(Long userId) {
        return jpa.findById(userId);
    }

    @Override
    public Optional<UserSchema> findByEmail(String emailAddress) {
        return jpa.findByEmailAddress(emailAddress);
    }
}