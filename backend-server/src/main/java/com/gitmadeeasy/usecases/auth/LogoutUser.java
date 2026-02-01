package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;

public class LogoutUser {
    private final TokenGateway tokenGateway;

    public LogoutUser(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    public void execute(String token) {
        tokenGateway.invalidateToken(token);
    }
}