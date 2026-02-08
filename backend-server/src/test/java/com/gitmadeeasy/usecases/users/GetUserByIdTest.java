package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdTest {
    @Mock private UserGateway userGateway;
    @InjectMocks private GetUserById getUserById;

    @Test
    @DisplayName("Get A User By ID - A User Exists With The Given ID")
    void execute_WhenUserWithIdExists_ReturnsUser() {
        // Arrange
        User existingUser = new User("1", "Alessio", "Cocuzza",
                "myemail1@gmail.com", "MyPassword123'");
        when(this.userGateway.getUserById(any(String.class))).thenReturn(Optional.of(existingUser));

        // Act
        User foundUser = getUserById.execute("1");

        // Assert
        assertEquals("1", foundUser.getUserId());
        assertEquals("Alessio", foundUser.getFirstName());
        assertEquals("Cocuzza", foundUser.getLastName());
        assertEquals("myemail1@gmail.com", foundUser.getEmailAddress());
    }

    @Test
    @DisplayName("Get A User By ID - A User Does Not Exist With The Given ID")
    void execute_WhenUserWithIdDoesNotExist_ThrowsError() {
        // Act & Assert
        assertThrows(UserNotFoundWithIdException.class, () -> getUserById.execute("0"));
    }
}