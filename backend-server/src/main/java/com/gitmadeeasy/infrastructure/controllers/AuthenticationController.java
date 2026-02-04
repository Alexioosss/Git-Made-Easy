package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final LoginUser loginUser;
    private final LogoutUser logoutUser;
    private final RefreshToken refreshToken;

    public AuthenticationController(LoginUser loginUser, LogoutUser logoutUser, RefreshToken refreshToken) {
        this.loginUser = loginUser;
        this.logoutUser = logoutUser;
        this.refreshToken = refreshToken;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthToken authToken = loginUser.execute(loginRequest);
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request) {
        String token = (String) request.getAttribute("jwt");
        logoutUser.execute(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthToken> refreshToken(HttpServletRequest request) {
        String token = (String) request.getAttribute("jwt");
        AuthToken authToken = refreshToken.execute(token);
        return ResponseEntity.ok(authToken);
    }
}