package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.InvalidUserDataException;

public class CreateUser {
    private final UserGateway userGateway;
    private final PasswordHasher passwordHasher;

    public CreateUser(UserGateway userGateway, PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.passwordHasher = passwordHasher;
    }

    public User execute(CreateUserRequest request) {
        if(request.firstName() == null || request.firstName().isBlank()) {
            throw new InvalidUserDataException("First Name Cannot Be Left Blank.");
        }

        if(request.lastName() == null || request.lastName().isBlank()) {
            throw new InvalidUserDataException("Last Name Cannot Be Left Blank.");
        }

        if(request.emailAddress() == null || request.emailAddress().isBlank()) {
            throw new InvalidUserDataException("Email Address Cannot Be Left Blank.");
        }

        if(request.password() == null || request.password().isBlank()) {
            throw new InvalidUserDataException("Password Cannot Be Left Blank.");
        }

        // Implement Email Verification Here To Ensure Email Address Is Valid

        // Hash the password for security
        String hashedPassword = this.passwordHasher.hash(request.password());

        return this.userGateway.createUser(
            new User(
                request.firstName(),
                request.lastName(),
                request.emailAddress(),
                hashedPassword
            )
        );
    }
}