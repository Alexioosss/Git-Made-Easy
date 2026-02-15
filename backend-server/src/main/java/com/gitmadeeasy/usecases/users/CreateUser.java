package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.EmailSender;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;

public class CreateUser {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;
    private final PasswordHasher passwordHasher;
    private final EmailSender emailSender;

    public CreateUser(UserGateway userGateway, UserIdentityProvider identityProvider,
                      PasswordHasher passwordHasher, EmailSender emailSender) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
        this.passwordHasher = passwordHasher;
        this.emailSender = emailSender;
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
        if(this.userGateway.existsByEmailAddress(request.emailAddress())) {
            throw new DuplicatedEmailException(request.emailAddress());
        }

        // Let firebase create the user, hashing the password and verifying the email via email verification link
        String firebaseId = this.identityProvider.createUser(
                request.firstName(), request.lastName(),
                request.emailAddress(), request.password());
        String hashedPassword = passwordHasher.hash(request.password());
        String verificationLink = this.identityProvider.generateEmailVerificationLink(request.emailAddress());
        this.emailSender.send(request.emailAddress(), "Verify your email address",
                "Click the link to verify your email: " + verificationLink);

        User newUser = new User(
                firebaseId, request.firstName(),
                request.lastName(), request.emailAddress(), false);

        this.userGateway.createUser(newUser, hashedPassword);
        return newUser;
    }
}