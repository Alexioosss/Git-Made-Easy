package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
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

    public User createNewUser(UserRequest request) {
        if(request.firstName().isEmpty()) {
            throw new IllegalArgumentException("First Name Cannot Be Left Blank.");
        }

        if(request.lastName().isEmpty()) {
            throw new IllegalArgumentException("Last Name Cannot Be Left Blank.");
        }

        if(request.emailAddress().isEmpty()) {
            throw new IllegalArgumentException("Email Address Cannot Be Left Blank.");
        }

        // Implement Email Verification Here To Ensure Email Address Is Valid

        return this.userGateway.createUser(
            this.userFactory.createUserFromRequest(
                request.firstName(),
                request.lastName(),
                request.emailAddress()
            )
        );
    }
}