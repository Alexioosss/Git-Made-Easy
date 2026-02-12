package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import com.gitmadeeasy.usecases.validation.MissingRequiredFieldException;

public class CreateUser {
    private final UserGateway userGateway;
    private final PasswordHasher passwordHasher;

    public CreateUser(UserGateway userGateway, PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.passwordHasher = passwordHasher;
    }

    public User execute(CreateUserRequest request) {
        if(request.firstName() == null || request.firstName().isBlank()) {
            throw new MissingRequiredFieldException("first name cannot be left blank");
        }

        if(request.lastName() == null || request.lastName().isBlank()) {
            throw new MissingRequiredFieldException("last name cannot be left blank");
        }

        if(request.emailAddress() == null || request.emailAddress().isBlank()) {
            throw new MissingRequiredFieldException("email address cannot be left blank");
        }

        if(request.password() == null || request.password().isBlank()) {
            throw new MissingRequiredFieldException("password cannot be left blank");
        }

        // Ensure the email does not already exist in the database, meaning this email is already registered
        if(this.userGateway.existsByEmail(request.emailAddress())) {
            throw new DuplicatedEmailException(request.emailAddress());
        }

        // Email verification can be implemented here

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