package com.gitmadeeasy.infrastructure.gateways.users;

import java.util.Optional;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;

public class UserDatabaseGateway implements UserGateway {
    private final UserRepository userRepository;
    private final UserSchemaMapper userMapper;

    public UserDatabaseGateway(UserRepository userRepository, UserSchemaMapper userSchemaMapper) {
        this.userRepository = userRepository;
        this.userMapper = userSchemaMapper;
    }

    @Override
    public User createUser(User user) {
        UserSchema savedSchema = this.userRepository.save(
            this.userMapper.toSchema(user)
        );
        return this.userMapper.toEntity(savedSchema);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return this.userRepository.findById(userId)
            .map(this.userMapper::toEntity);
    }

    @Override
    public Optional<User> getUserByEmailAddress(String emailAddress) {
        return this.userRepository.findByEmail(emailAddress)
                .map(this.userMapper::toEntity);
    }
}