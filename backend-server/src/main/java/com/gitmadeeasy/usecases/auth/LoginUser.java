package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.auth.exceptions.InvalidPasswordException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;

public class LoginUser {
    private final UserGateway userGateway;
    private final PasswordHasher passwordHasher;
    private final TokenGateway tokenGateway;

    public LoginUser(UserGateway userGateway, PasswordHasher passwordHasher, TokenGateway tokenGateway) {
        this.userGateway = userGateway;
        this.passwordHasher = passwordHasher;
        this.tokenGateway = tokenGateway;
    }

    public AuthToken execute(LoginRequest request) {
        User user = this.userGateway.getUserByEmailAddress(request.email())
                .orElseThrow(() -> new UserNotFoundWithEmailException(request.email()));

        if(!passwordHasher.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = this.tokenGateway.generateToken(user);
        return new AuthToken(accessToken);
    }
}