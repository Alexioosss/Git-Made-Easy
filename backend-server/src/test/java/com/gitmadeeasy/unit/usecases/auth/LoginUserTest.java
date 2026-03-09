package com.gitmadeeasy.unit.usecases.auth;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.LoginUser;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.auth.dto.AuthToken;
import com.gitmadeeasy.usecases.auth.dto.LoginRequest;
import com.gitmadeeasy.usecases.auth.exceptions.EmailNotVerifiedException;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserTest {
    @Mock private UserGateway userGateway;
    @Mock private TokenGateway tokenGateway;
    @Mock private UserIdentityProvider userIdentityProvider;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        boolean requireEmailVerification = true;
        loginUser = new LoginUser(userGateway, tokenGateway, userIdentityProvider);
    }


    @Test
    @DisplayName("Login User - Valid Credentials & Verified Email Returns AuthToken")
    void execute_WhenEmailVerified_ReturnsAuthToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        User user = new User("1", "John", "Doe", "myemail1@gmail.com");

        when(this.userIdentityProvider.login("myemail1@gmail.com", "MyPassword123'"))
                .thenReturn("firebase-myemail1@gmail.com");
        when(this.userGateway.getUserByEmailAddress("myemail1@gmail.com")).thenReturn(Optional.of(user));
        when(this.userIdentityProvider.isEmailVerified("firebase-myemail1@gmail.com")).thenReturn(true);
        when(this.tokenGateway.generateToken(user)).thenReturn("token");

        // Act
        AuthToken result = this.loginUser.execute(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("token", result.accessToken());
    }

    @Test
    @DisplayName("Login User - Email Not Verified Throws EmailNotVerifiedException")
    void execute_WhenEmailNotVerified_ThrowsEmailNotVerifiedException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");
        User user = new User("1", "John", "Doe", "myemail1@gmail.com");

        when(this.userIdentityProvider.login("myemail1@gmail.com", "MyPassword123'"))
                .thenReturn("firebase-myemail1@gmail.com");
        when(this.userGateway.getUserByEmailAddress("myemail1@gmail.com")).thenReturn(Optional.of(user));
        when(this.userIdentityProvider.isEmailVerified("firebase-myemail1@gmail.com")).thenReturn(false);

        // Act & Assert
        assertThrows(EmailNotVerifiedException.class, () -> this.loginUser.execute(loginRequest));
    }

    @Test
    @DisplayName("Login User - Email Not Found Throws InvalidCredentialsException")
    void execute_WhenEmailIsNotValid_ThrowsInvalidCredentialsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("myemail1@gmail.com", "MyPassword123'");

        when(this.userIdentityProvider.login("myemail1@gmail.com", "MyPassword123'"))
                .thenReturn("firebase-myemail1@gmail.com");
        when(this.userGateway.getUserByEmailAddress("myemail1@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> this.loginUser.execute(loginRequest));
    }
}