package com.gitmadeeasy.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.JpaUserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

import java.util.Optional;

public class JpaUserDatabaseGateway implements UserGateway {
    private final JpaUserRepository jpa;
    private final UserSchemaMapper userSchemaMapper;

    public JpaUserDatabaseGateway(JpaUserRepository jpa, UserSchemaMapper userSchemaMapper) {
        this.jpa = jpa;
        this.userSchemaMapper = userSchemaMapper;
    }

    @Override
    public User createUser(User newUser, String hashedPassword) {
        return this.userSchemaMapper.fromJpaSchema(this.jpa.save(this.userSchemaMapper.toJpaSchema(newUser, hashedPassword)));
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return this.jpa.findById(userId).map(this.userSchemaMapper::fromJpaSchema);
    }

    @Override
    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return this.jpa.findByEmailAddress(emailAddress).map(this.userSchemaMapper::fromJpaSchema);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return this.jpa.existsByEmailAddress(emailAddress);
    }
}