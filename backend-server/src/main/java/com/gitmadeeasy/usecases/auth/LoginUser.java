package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUser {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private static final Logger log = LoggerFactory.getLogger(LoginUser.class);

    public LoginUser(UserGateway userGateway, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
    }

    public AuthToken execute(LoginRequest request) {
        User user = this.userGateway.getUserByEmailAddress(request.email())
                .orElseThrow(InvalidCredentialsException::new);
        log.info("user found with emailAddress= {}", request.email());

        String accessToken = this.tokenGateway.generateToken(user);
        log.info("JWT Token generated successfully");
        return new AuthToken(accessToken);
    }
}