package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserById {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;
    private static final Logger log = LoggerFactory.getLogger(GetUserById.class);

    public GetUserById(UserGateway userGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
    }

    public User execute(String userId) {
        User user = this.userGateway.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundWithIdException(userId));
        log.info("User found successfully by its id");
        boolean verified = this.identityProvider.isEmailVerified(userId);
        log.info("User's email address verified= {}", verified);
        user.setEmailVerified(verified);
        return user;
    }
}