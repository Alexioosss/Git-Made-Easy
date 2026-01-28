package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;

public class CreateUser {
    private final UserGateway userGateway;

    public CreateUser(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(CreateUserRequest request) {
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
            new User(
                request.firstName(),
                request.lastName(),
                request.emailAddress()
            )
        );
    }
}