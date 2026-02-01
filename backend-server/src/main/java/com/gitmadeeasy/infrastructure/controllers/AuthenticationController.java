package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.auth.*;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AuthToken> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthToken authToken = loginUser.execute(loginRequest);
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        logoutUser.execute(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthToken> loginUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        AuthToken authToken = refreshToken.execute(token);
        return ResponseEntity.ok(authToken);
    }
}