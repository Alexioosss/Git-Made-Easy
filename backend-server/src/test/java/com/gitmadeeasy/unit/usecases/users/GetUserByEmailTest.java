package com.gitmadeeasy.unit.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByEmailTest {
    @Mock private UserGateway userGateway;
    @Mock private UserIdentityProvider identityProvider;
    @InjectMocks private GetUserByEmail getUserByEmail;


    @Test
    @DisplayName("Get A User By Email Address - A User Exists With The Given Email Address")
    void execute_WhenUserWithEmailExists_ReturnsUser() {
        // Arrange
        User existingUser = new User("1", "Alessio", "Cocuzza",
                "myemail1@gmail.com", false);
        when(this.userGateway.getUserByEmailAddress(any(String.class))).thenReturn(Optional.of(existingUser));
        when(this.identityProvider.isEmailVerified("1")).thenReturn(true);

        // Act
        User foundUser = getUserByEmail.execute("myemail1@gmail.com");

        // Assert
        assertEquals("1", foundUser.getUserId());
        assertEquals("Alessio", foundUser.getFirstName());
        assertEquals("Cocuzza", foundUser.getLastName());
        assertEquals("myemail1@gmail.com", foundUser.getEmailAddress());
        assertTrue(foundUser.isEmailVerified());
    }

    @Test
    @DisplayName("Get A User By Email Address - A User Does Not Exist With The Given Email Address")
    void execute_WhenUserWithEmailDoesNotExist_ThrowsError() {
        // Arrange
        when(this.userGateway.getUserByEmailAddress("undefinedemail@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundWithEmailException.class, () -> getUserByEmail.execute("undefinedemail@gmail.com"));
    }
}