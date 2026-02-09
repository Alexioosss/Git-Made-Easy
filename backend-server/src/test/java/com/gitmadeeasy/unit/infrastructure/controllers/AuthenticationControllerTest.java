package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockitoBean private LoginUser loginUser;
    @MockitoBean private LogoutUser logoutUser;
    @MockitoBean private RefreshToken refreshToken;


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
        String malformedRequestPayload = "{ \"mail\":, \"test\":, ";

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
}