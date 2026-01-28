package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;

import java.util.Optional;

public class GetUserByEmail {
    private final UserGateway userGateway;

    public GetUserByEmail(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String emailAddress) {
        return this.userGateway.getUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundWithEmailException(emailAddress));
    }
}