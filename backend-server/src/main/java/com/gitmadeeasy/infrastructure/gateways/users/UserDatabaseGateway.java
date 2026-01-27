package com.gitmadeeasy.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import com.gitmadeeasy.infrastructure.factories.users.UserFactory;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;

public class UserDatabaseGateway implements UserGateway {
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public UserDatabaseGateway(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    @Override
    public void createUser(UserRequest request) {
        this.userRepository.save(
                this.userFactory.createSchemaFromRequest(request)
        );
    }

    @Override
    public void getUser(Long userId) {
    }

    @Override
    public void deleteUser(Long userId) {
    }
}