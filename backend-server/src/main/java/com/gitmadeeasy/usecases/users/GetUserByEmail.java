package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserByEmail {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;
    private static final Logger log = LoggerFactory.getLogger(GetUserByEmail.class);

    public GetUserByEmail(UserGateway userGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
    }

    public User execute(String emailAddress) {
        // Try and find the user by the email address, or throw an exception for user not found by the given email address
        User user = this.userGateway.getUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundWithEmailException(emailAddress));
        log.info("User found successfully by their email address");
        boolean verified = this.identityProvider.isEmailVerified(user.getUserId()); // Also check if the email is verified
        log.info("User's email address verified: {}", verified); // Log the verification status of the email address
        return user;
    }
}