package com.gitmadeeasy.infrastructure.gateways.users;

import java.util.Optional;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

public class UserDatabaseGateway implements UserGateway {
    private final UserRepository userRepository;
    private final UserSchemaMapper userSchemaMapper;

    public UserDatabaseGateway(UserRepository userRepository, UserSchemaMapper userSchemaMapper) {
        this.userRepository = userRepository;
        this.userSchemaMapper = userSchemaMapper;
    }

    @Override
    public User createUser(User newUser) {
        UserSchema savedUserSchema = this.userRepository.save(
            this.userSchemaMapper.toSchema(newUser)
        );
        return this.userSchemaMapper.toEntity(savedUserSchema);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return this.userRepository.findById(userId)
            .map(this.userSchemaMapper::toEntity);
    }

    @Override
    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return this.userRepository.findByEmail(emailAddress)
                .map(this.userSchemaMapper::toEntity);
    }

    @Override
    public boolean existsByEmail(String emailAddress) {
        return this.userRepository.existsByEmail(emailAddress);
    }
}