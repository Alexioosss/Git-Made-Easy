package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;

public class LoginUser {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private final PasswordHasher passwordHasher;

    public LoginUser(UserGateway userGateway, TokenGateway tokenGateway, PasswordHasher passwordHasher) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
        this.passwordHasher = passwordHasher;
    }

    public AuthToken execute(LoginRequest request) {
        User user = this.userGateway.getUserByEmailAddress(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if(!this.passwordHasher.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = this.tokenGateway.generateToken(user);
        return new AuthToken(accessToken);
    }
}