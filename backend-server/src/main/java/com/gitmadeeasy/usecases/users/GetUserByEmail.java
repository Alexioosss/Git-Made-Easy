package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;

public class GetUserByEmail {
    private final UserGateway userGateway;
    private final UserIdentityProvider identityProvider;

    public GetUserByEmail(UserGateway userGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.identityProvider = identityProvider;
    }

    public User execute(String emailAddress) {
        User user = this.userGateway.getUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundWithEmailException(emailAddress));
        boolean verified = this.identityProvider.isEmailVerified(user.getUserId());
        user.setEmailVerified(verified);
        return user;
    }

    public UserIdentityProvider getIdentityProvider() {
        return identityProvider;
    }
}