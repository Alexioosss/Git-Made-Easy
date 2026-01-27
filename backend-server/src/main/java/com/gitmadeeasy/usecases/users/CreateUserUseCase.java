package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.dto.users.UserRequest;

public class CreateUserUseCase {
    private final UserGateway userGateway;

    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    void createUser(UserRequest request) {
    }
}