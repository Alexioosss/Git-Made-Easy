package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutUserTest {
    private TokenGateway tokenGateway;
    private LogoutUser logoutUser;

    @BeforeEach
    void setUp() {
        tokenGateway = mock(TokenGateway.class);
        logoutUser = new LogoutUser(tokenGateway);
    }

    @Test
    @DisplayName("Logout User - Invalidates Token")
    void execute_WhenCalled_InvalidatesTokenViaGateway() {
        // Arrange
        String token = "token";

        // Act
        logoutUser.execute(token);

        // Assert
        verify(tokenGateway, times(1)).invalidateToken(token);
    }
}