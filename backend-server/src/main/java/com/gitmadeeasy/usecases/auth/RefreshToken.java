package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.exceptions.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshToken {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private static final Logger log = LoggerFactory.getLogger(RefreshToken.class);

    public RefreshToken(UserGateway userGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
    }

    public AuthToken execute(String token) {
        User user = this.userGateway.getUserByEmailAddress(this.tokenGateway.getUserIdFromToken(token))
                .orElseThrow(InvalidTokenException::new);
        log.info("Found user via the userID, parsed from the JWT Token claims");

        return new AuthToken(this.tokenGateway.refreshToken(user));
    }
}