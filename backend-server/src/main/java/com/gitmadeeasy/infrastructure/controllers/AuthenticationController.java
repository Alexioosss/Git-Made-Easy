package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.auth.exceptions.InvalidTokenException;
import com.gitmadeeasy.usecases.users.GetUserById;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController @RequestMapping("/auth")
public class AuthenticationController {
    private final LoginUser loginUser;
    private final LogoutUser logoutUser;
    private final RefreshToken refreshToken;
    private final GetUserById getUserById;
    private final UserResponseMapper userResponseMapper;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(LoginUser loginUser, LogoutUser logoutUser, RefreshToken refreshToken,
                                    GetUserById getUserById, UserResponseMapper userResponseMapper) {
        this.loginUser = loginUser;
        this.logoutUser = logoutUser;
        this.refreshToken = refreshToken;
        this.getUserById = getUserById;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthToken> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                               HttpServletResponse response) {
        log.info("POST /login - Logging a user in with email address={}", loginRequest.email());

        AuthToken authToken = this.loginUser.execute(loginRequest);
        setAuthenticationCookie(response, authToken.accessToken());

        log.info("User logged in successfully.");
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /logout - Logging out a user using their JWT Token from the request");
        String token = (String) request.getAttribute("jwt");

        this.logoutUser.execute(token);
        clearAuthenticationCookie(response);

        log.info("User logged out successfully.");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthToken> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("POST /refresh - Refreshing a user's JWT Token");
        String token = (String) request.getAttribute("jwt");
        if(token == null || token.isBlank()) { throw new InvalidTokenException(); }

        AuthToken newToken = this.refreshToken.execute(token);
        setAuthenticationCookie(response, newToken.accessToken());

        log.info("User's JWT Token refreshed successfully.");
        return ResponseEntity.ok(newToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        if(principal == null) { return ResponseEntity.status(401).build(); } // The token is expired but still valid
        User foundUser = this.getUserById.execute(principal.getName());
        UserResponse userResponse = this.userResponseMapper.toUserResponse(foundUser);
        return ResponseEntity.ok(userResponse);
    }


    private void setAuthenticationCookie(HttpServletResponse response, String token) {
        response.addHeader("Set-Cookie",
                String.format("access_token=%s; HttpOnly; Secure; Path=/; Max-Age=%d; SameSite=None",
                        token, 60 * 60));
    }

    private void clearAuthenticationCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", "access_token=; HttpOnly; Secure; Path=/; Max-Age=0; SameSite=None");
    }
}