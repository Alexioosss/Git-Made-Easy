package com.gitmadeeasy.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;

public class UserDatabaseGateway implements UserGateway {
    private final UserRepository userRepository;

    public UserDatabaseGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserSchema schema) {
        this.userRepository.save(schema);
    }

    @Override
    public void getUser(Long userId) {
    }

    @Override
    public void deleteUser(Long userId) {
    }
}