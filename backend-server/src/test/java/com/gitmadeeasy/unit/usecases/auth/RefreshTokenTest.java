package com.gitmadeeasy.unit.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.RefreshToken;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {
    @Mock private TokenGateway tokenGateway;
    @Mock private UserGateway userGateway;
    @InjectMocks private RefreshToken refreshToken;


    @Test
    @DisplayName("Refresh Token - Returns New AuthToken When Old Token Is Invalid")
    void execute_WhenTokenIsValid_ReturnsNewAuthToken() {
        // Arrange
        String oldToken = "token", userId = "1";
        User user = new User("1", "John", "Doe", "myemail1@gmail.com", false);
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