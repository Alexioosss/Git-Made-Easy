package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;

public class GetUserById {
    private final UserGateway userGateway;

    public GetUserById(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String userId) {
        return this.userGateway.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundWithIdException(userId));
    }
}