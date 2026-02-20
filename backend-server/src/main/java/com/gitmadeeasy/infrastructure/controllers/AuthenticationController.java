package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/auth")
public class AuthenticationController {
    private final LoginUser loginUser;
    private final LogoutUser logoutUser;
    private final RefreshToken refreshToken;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(LoginUser loginUser, LogoutUser logoutUser, RefreshToken refreshToken) {
        this.loginUser = loginUser;
        this.logoutUser = logoutUser;
        this.refreshToken = refreshToken;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("POST /login - Logging a user in with email address={}", loginRequest.email());
        System.out.println("Email Address: " + loginRequest.email() + ", Password: " + loginRequest.password());
        AuthToken authToken = loginUser.execute(loginRequest);
        log.info("User logged in successfully.");
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        log.info("POST /logout - Logging out a user using their JWT Token from the request");
        String token = (String) request.getAttribute("jwt");
        logoutUser.execute(token);
        log.info("User logged out successfully.");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthToken> refreshToken(HttpServletRequest request) {
        log.info("POST /logout - Refreshing a user's JWT Token");
        String token = (String) request.getAttribute("jwt");
        AuthToken authToken = refreshToken.execute(token);
        log.info("User's JWT Token refreshed successfully.");
        return ResponseEntity.ok(authToken);
    }
}