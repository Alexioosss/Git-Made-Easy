package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.auth.exceptions.EmailNotVerifiedException;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginUser {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private final UserIdentityProvider identityProvider;
    private static final Logger log = LoggerFactory.getLogger(LoginUser.class);

    public LoginUser(UserGateway userGateway, TokenGateway tokenGateway, UserIdentityProvider identityProvider) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
        this.identityProvider = identityProvider;
    }

    public AuthToken execute(LoginRequest request) {
        String firebaseUid = this.identityProvider.login(request.email(), request.password());
        log.info("Firebase login successful for Email Address {}", request.email());

        User user = this.userGateway.getUserByEmailAddress(request.email())
                .orElseThrow(InvalidCredentialsException::new);
        user.setFirebaseUid(firebaseUid);
        log.info("User found with Email Address {}", request.email());

        if(!this.identityProvider.isEmailVerified(firebaseUid)) {
            throw new EmailNotVerifiedException();
        }

        String accessToken = this.tokenGateway.generateToken(user);
        log.info("JWT Token generated successfully");
        return new AuthToken(accessToken);
    }
}