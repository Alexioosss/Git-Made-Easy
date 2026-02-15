package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;

public class GetUserById {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;

    public GetUserById(UserGateway userGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
    }

    public User execute(String userId) {
        User user = this.userGateway.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundWithIdException(userId));
        boolean verified = this.identityProvider.isEmailVerified(userId);
        user.setEmailVerified(verified);
        return user;
    }
}