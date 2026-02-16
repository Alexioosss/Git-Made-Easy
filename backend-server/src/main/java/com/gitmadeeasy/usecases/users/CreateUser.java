package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUser {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;
    private static final Logger log = LoggerFactory.getLogger(CreateUser.class);

    public CreateUser(UserGateway userGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
    }

    public User execute(CreateUserRequest request) {
        if(request.firstName() == null || request.firstName().isBlank()) {
            log.warn("CreateUser failed: missing firstName field from request");
            throw new MissingRequiredFieldException("first name cannot be left blank");
        }
        if(request.lastName() == null || request.lastName().isBlank()) {
            log.warn("CreateUser failed: missing lastName field from request");
            throw new MissingRequiredFieldException("last name cannot be left blank");
        }
        if(request.emailAddress() == null || request.emailAddress().isBlank()) {
            log.warn("CreateUser failed: missing emailAddress field from request");
            throw new MissingRequiredFieldException("email address cannot be left blank");
        }
        if(request.password() == null || request.password().isBlank()) {
            log.warn("CreateUser failed: missing password field from request");
            throw new MissingRequiredFieldException("password cannot be left blank");
        }

        // Ensure the email does not already exist in the database, meaning this email is already registered
        if(this.userGateway.existsByEmailAddress(request.emailAddress())) {
            log.warn("CreateUser failed: duplicated emailAddress");
            throw new DuplicatedEmailException(request.emailAddress());
        }

        // Let firebase create the user and hash the password
        String firebaseUid = this.identityProvider.createUser(
                request.firstName(), request.lastName(),
                request.emailAddress(), request.password());

        User userToSave = new User(firebaseUid, request.firstName(), request.lastName(), request.emailAddress());

        User createdUser = this.userGateway.createUser(userToSave);
        log.info("User persisted successfully. userID={}, emailAddress={}", createdUser.getUserId(), createdUser.getEmailAddress());

        this.identityProvider.sendVerificationEmail(request.emailAddress());
        log.info("Verification email sent to {}", request.emailAddress());

        return createdUser;
    }
}