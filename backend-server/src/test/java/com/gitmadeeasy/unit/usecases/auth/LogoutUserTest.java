package com.gitmadeeasy.unit.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.usecases.auth.LogoutUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogoutUserTest {
    @Mock private TokenGateway tokenGateway;
    @InjectMocks private LogoutUser logoutUser;


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