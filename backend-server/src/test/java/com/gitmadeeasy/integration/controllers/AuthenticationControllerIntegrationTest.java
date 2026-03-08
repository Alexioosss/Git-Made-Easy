package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.infrastructure.gateways.users.JpaUserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.JpaUserRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired JpaUserRepository userRepository;

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
        LoginRequest request = new LoginRequest("emailnotfound@gmail.com", "WrongPassword");

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
        JpaUserSchema user = userRepository.findByEmailAddress("myemail1@gmail.com").get();
        String expiredToken = generateExpiredToken(user, "TEST_ONLY_32_CHAR_SECRET_KEY_ABCDEF123456!");

        // Act & Assert
        this.mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + expiredToken))
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

    @Test
    @DisplayName("Refresh Token - Blank Token Returns 401")
    void refreshToken_WhenTokenBlank_ThrowsInvalidTokenException() throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/auth/refresh")
                        .requestAttr("jwt", ""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get Current User - With Valid Token Returns UserResponse")
    void getCurrentUser_WithValidToken_ReturnsUserResponse() throws Exception {
        // Arrange
        saveMockUserInDataStore();
        JpaUserSchema user = this.userRepository.findByEmailAddress("myemail1@gmail.com").get();
        LoginRequest loginRequest = new LoginRequest(user.getEmailAddress(), "MyPassword123'");
        String loginResponse = this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String accessToken = JsonUtil.readJson(loginResponse, "accessToken");

        // Act & Assert
        this.mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + accessToken)
                        .principal(() -> user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Current User - Principal Is Null Returns 401")
    void getCurrentUser_WhenPrincipalNull_ReturnsUnauthorized() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }



    // ----- HELPER METHODS ----- //


    private void saveMockUserInDataStore() {
        String emailAddress = "myemail1@gmail.com";
        this.userRepository.save(new JpaUserSchema(
               "firebase-" + emailAddress,
                        "John", "Doe",
                        emailAddress));
        System.out.println("Mock User Saved Successfully.");
    }

    private String generateExpiredToken(JpaUserSchema user, String secret) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().setId(UUID.randomUUID().toString())
                .setSubject(user.getId())
                .claim("email",user.getEmailAddress())
                .claim("firebaseUid",user.getFirebaseUid())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // issued 1 hour ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 45)) // expired 45 mins ago
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}