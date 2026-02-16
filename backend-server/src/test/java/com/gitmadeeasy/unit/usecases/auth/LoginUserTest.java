package com.gitmadeeasy.unit.usecases.auth;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
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
class LoginUserTest {
    @Mock private UserGateway userGateway;
    @Mock private TokenGateway tokenGateway;
    @Mock private PasswordHasher passwordHasher;
    @InjectMocks private LoginUser loginUser;


    @Test
    @DisplayName("Login User - Valid Credentials Returns AuthToken")
    void execute_WhenValidCredentials_ReturnsAuthToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        User user = new User("1", "John", "Doe", "myemail1@gmail.com",
                "HashedMyPassword123'", false);

        when(this.userGateway.getUserByEmailAddress("myemail1@gmail.com")).thenReturn(Optional.of(user));
        when(this.passwordHasher.matches("MyPassword123'", user.getPassword())).thenReturn(true);
        when(this.tokenGateway.generateToken(user)).thenReturn("token");

        // Act
        AuthToken result = this.loginUser.execute(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("token", result.accessToken());
    }

    @Test
    @DisplayName("Login User - Email Is Not Valid Throws Exception")
    void execute_WhenEmailIsNotValid_ThrowsInvalidCredentialsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        when(this.userGateway.getUserByEmailAddress("myemail1@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> this.loginUser.execute(loginRequest));
    }
}