package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {
    private TokenGateway tokenGateway;
    private UserGateway userGateway;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        tokenGateway = mock(TokenGateway.class);
        userGateway = mock(UserGateway.class);
        refreshToken = new RefreshToken(userGateway, tokenGateway);
    }

    @Test
    @DisplayName("Refresh Token - Returns New AuthToken When Old Token Is Invalid")
    void execute_WhenTokenIsValid_ReturnsNewAuthToken() {
        // Arrange
        String oldToken = "token", userId = "1";
        User user = new User("John", "Doe", "myemail1@gmail.com", "MyPassword123'");
        when(this.tokenGateway.getUserIdFromToken(oldToken)).thenReturn(userId);
        when(this.userGateway.getUserById(userId)).thenReturn(Optional.of(user));
        when(this.tokenGateway.refreshToken(user)).thenReturn("refreshed-token");

        // Act
        AuthToken refreshedToken = this.refreshToken.execute(oldToken);

        // Assert
        assertNotNull(refreshedToken);
        assertEquals("refreshed-token", refreshedToken.accessToken());
    }

    @Test
    @DisplayName("Refresh Token - Returns New AuthToken")
    void execute_WhenTokenIsInvalid_ThrowsRuntimeException() {
        // Arrange
        String oldToken = "token", userId = "1";
        when(this.tokenGateway.getUserIdFromToken(oldToken)).thenReturn(userId);
        when(this.userGateway.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.refreshToken.execute(oldToken));
    }
}