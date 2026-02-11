package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.infrastructure.gateways.users.UserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired LoginUser loginUser;
    @Autowired LogoutUser logoutUser;
    @Autowired RefreshToken refreshToken;
    @Autowired UserRepository userRepository;
    @Autowired PasswordHasher passwordHasher;

    @BeforeEach
    public void setUp() { this.userRepository.deleteAll(); }


    @Test
    @DisplayName("Login User - Valid Credentials")
    void loginUser_WhenValidCredentials_ReturnsAuthToken() throws Exception {
        // Arrange
        saveMockUserInDataStore();
        LoginRequest request = new LoginRequest("myemail1@gmail.com", "MyPassword123'");

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("Login User - Invalid Credentials")
    void loginUser_WhenInvalidCredentials_ThrowsException() throws Exception {
        // Arrange
        saveMockUserInDataStore();
        LoginRequest request = new LoginRequest("myemail1@gmail.com", "WrongPassword");

        // Act & Assert
        this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Logout User - Valid Token Present")
    void logoutUser_WhenTokenPresent_RemovesAndInvalidatesToken() throws Exception {
        // Arrange
        saveMockUserInDataStore();
        LoginRequest request = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        String loginResponse = this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(request)))
                .andReturn().getResponse().getContentAsString();
        String accessToken = JsonUtil.readJson(loginResponse, "accessToken");

        // Act & Assert
        this.mockMvc.perform(post("/auth/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
        // Assert Again
        this.mockMvc.perform(post("/auth/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Logout User - No Token Present")
    void logoutUser_WhenTokenNotPresent_ThrowsException() throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Refresh Token - Expired Token Present")
    void refreshToken_WhenCurrentTokenExpired_ReturnsRefreshedToken() throws Exception {
        // Arrange
        saveMockUserInDataStore();
        LoginRequest request = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        String loginResponse = this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(request)))
                .andReturn().getResponse().getContentAsString();
        String accessToken = JsonUtil.readJson(loginResponse, "accessToken");

        // Act & Assert
        this.mockMvc.perform(post("/auth/refresh")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("Refresh Token - No Token Present")
    void refreshToken_WhenNoTokenPresent_ThrowsException() throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }



    // ----- HELPER METHODS ----- //


    private void saveMockUserInDataStore() {
        this.userRepository.save(
                new UserSchema(
                        "John", "Doe",
                        "myemail1@gmail.com",
                        passwordHasher.hash("MyPassword123'")
                )
        );
        System.out.println("Mock User Saved Successfully.");
    }
}