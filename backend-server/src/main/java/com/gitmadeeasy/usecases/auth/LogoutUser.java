package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogoutUser {
    private final TokenGateway tokenGateway;
    private static final Logger log = LoggerFactory.getLogger(LogoutUser.class);

    public LogoutUser(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    public void execute(String token) {
        this.tokenGateway.invalidateToken(token);
        log.info("JWT Token invalidated successfully.");
    }
}