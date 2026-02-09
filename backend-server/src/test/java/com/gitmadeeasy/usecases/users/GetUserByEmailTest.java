package com.gitmadeeasy.usecases.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
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
class GetUserByEmailTest {
    @Mock private UserGateway userGateway;
    @InjectMocks private GetUserByEmail getUserByEmail;


    @Test
    @DisplayName("Get A User By Email Address - A User Exists With The Given Email Address")
    void execute_WhenUserWithEmailExists_ReturnsUser() {
        // Arrange
        User existingUser = new User("1", "Alessio", "Cocuzza",
                "myemail1@gmail.com", "MyPassword123'");
        when(this.userGateway.getUserByEmailAddress(any(String.class))).thenReturn(Optional.of(existingUser));

        // Act
        User foundUser = getUserByEmail.execute("myemail1@gmail.com");

        // Assert
        assertEquals("1", foundUser.getUserId());
        assertEquals("Alessio", foundUser.getFirstName());
        assertEquals("Cocuzza", foundUser.getLastName());
        assertEquals("myemail1@gmail.com", foundUser.getEmailAddress());
    }

    @Test
    @DisplayName("Get A User By Email Address - A User Does Not Exist With The Given Email Address")
    void execute_WhenUserWithEmailDoesNotExist_ThrowsError() {
        // Act & Assert
        assertThrows(UserNotFoundWithEmailException.class, () -> getUserByEmail.execute("undefinedemail@gmail.com"));
    }
}