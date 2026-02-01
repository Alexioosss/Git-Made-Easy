package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;

public class RefreshToken {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;

    public RefreshToken(UserGateway userGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
    }

    public AuthToken execute(String token) {
        User user = this.userGateway.getUserById(
                this.tokenGateway.getUserIdFromToken(token)
        ).orElseThrow(() -> new RuntimeException("Invalid Token."));

        return new AuthToken(
                this.tokenGateway.refreshToken(user)
        );
    }
}