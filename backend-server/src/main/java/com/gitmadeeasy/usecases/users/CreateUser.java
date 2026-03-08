package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.email.VerificationEmailService;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUser {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;
    private final VerificationEmailService emailVerificationService;
    private static final Logger log = LoggerFactory.getLogger(CreateUser.class);

    public CreateUser(UserGateway userGateway, UserIdentityProvider identityProvider,
                      VerificationEmailService emailVerificationService) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
        this.emailVerificationService = emailVerificationService;
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
        log.info("User persisted successfully. User ID {}, Email Address {}", createdUser.getUserId(), createdUser.getEmailAddress());

        this.emailVerificationService.sendVerificationEmail(request.emailAddress(), request.firstName());
//
//        String verificationLink = this.identityProvider.generateVerificationEmail(request.emailAddress());
//        log.info("Verification email generated successfully");
//
//        this.emailSender.send(request.emailAddress(), "Verify your email",
//                "Welcome to GitMadeEasy!\n\nPlease verify your email using the link below:\n\n" + verificationLink);
//        log.info("Verification email sent to {}", request.emailAddress());

        return createdUser;
    }
}