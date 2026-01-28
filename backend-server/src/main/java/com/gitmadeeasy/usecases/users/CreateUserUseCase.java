package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import com.gitmadeeasy.infrastructure.factories.users.UserFactory;

public class CreateUserUseCase {
    private final UserGateway userGateway;
    private final UserFactory userFactory;

    public CreateUserUseCase(UserGateway userGateway, UserFactory userFactory) {
        this.userGateway = userGateway;
        this.userFactory = userFactory;
    }

    void createUser(UserRequest request) {
        this.userGateway.createUser(
            this.userFactory.createSchemaFromRequest(request)
        );
    }
}