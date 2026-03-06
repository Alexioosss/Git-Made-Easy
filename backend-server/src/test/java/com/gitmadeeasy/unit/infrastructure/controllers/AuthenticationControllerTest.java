package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.controllers.AuthenticationController;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.users.GetUserById;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private LoginUser loginUser;
    @MockitoBean private LogoutUser logoutUser;
    @MockitoBean private RefreshToken refreshToken;
    @MockitoBean private GetUserById getUserById;
    @MockitoBean private UserResponseMapper userResponseMapper;


    @Test
    @DisplayName("Login User - Valid Credentials - Returns Authentication Token & 200")
    void loginUser_WhenValidCredentials_ReturnsAuthenticationToken() throws Exception {
        // Arrange
        LoginRequest validLoginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        AuthToken token = new AuthToken("MyToken123'");
        when(this.loginUser.execute(validLoginRequest)).thenReturn(token);

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(token)));
    }

    @Test
    @DisplayName("Login User - Missing Credentials - Returns 400")
    void loginUser_WhenMissingCredentials_ReturnsBadRequest() throws Exception {
        // Arrange
        LoginRequest invalidLoginRequest = new LoginRequest("", "");

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidLoginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login User - Null Credentials - Returns 400")
    void loginUser_WhenNullCredentials_ReturnsBadRequest() throws Exception {
        // Arrange
        LoginRequest invalidLoginRequest = new LoginRequest(null, null);

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidLoginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login User - Malformed Request Payload - Returns 400")
    void loginUser_WhenMalformedPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        String malformedRequestPayload = "{ \"mail\":, \"pass\":, ";

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedRequestPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Logout User - Token Is Present - Returns 204 No Content")
    void logoutUser_WhenTokenIsPresent_ReturnsNoContent() throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/auth/logout")
                        .requestAttr("jwt", "my-secure-jwt-token"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Refresh Token - Old Token Is Present - Returns Refreshed Token")
    void refreshToken_WhenOldTokenIsPresent_ReturnsRefreshedToken() throws Exception {
        // Arrange
        AuthToken refreshedToken = new AuthToken("refreshed-jwt-token");
        when(this.refreshToken.execute("my-old-jwt-token")).thenReturn(refreshedToken);

        // Act & Assert
        this.mockMvc.perform(post("/auth/refresh")
                        .requestAttr("jwt", "my-old-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(refreshedToken)));
    }

    @Test
    @DisplayName("Get Current User - Returns UserResponse and 200")
    void getCurrentUser_WhenPrincipalIsPresent_ReturnsUserResponse() throws Exception {
        // Arrange
        String userId = "firebase-myemail1@gmail.com";
        Principal principal = () -> userId;

        User mockUser = new User(userId, "John", "Doe", "myemail1@gmail.com");
        UserResponse mockResponse = new UserResponse(userId, "John", "Doe", "myemail1@gmail.com");

        when(this.getUserById.execute(userId)).thenReturn(mockUser);
        when(this.userResponseMapper.toUserResponse(mockUser)).thenReturn(mockResponse);

        // Act & Assert
        this.mockMvc.perform(get("/auth/me").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(mockResponse)));
    }

    @Test
    @DisplayName("Get Current User - No Principal - Returns 401")
    void getCurrentUser_WhenPrincipalIsNull_ReturnsUnauthorized() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}