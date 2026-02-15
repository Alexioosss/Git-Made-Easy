package com.gitmadeeasy.unit.usecases.users;

import com.gitmadeeasy.entities.security.PasswordHasher;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {
    @Mock private UserGateway userGateway;
    @Mock private UserIdentityProvider identityProvider;
    @Mock private PasswordHasher passwordHasher;
    @InjectMocks private CreateUser createUser;

    private static final String RAW_PASSWORD = "MyPassword123'";
    private static final String HASHED_PASSWORD = "HashedMyPassword123'";
    private static final String FIREBASE_ID = "firebase-123";


    @Test
    @DisplayName("Create A User - Valid Payload")
    void execute_WhenValidPayload_ReturnsCreatedUser() {
        // Arrange
        CreateUserRequest request = provideValidUserRequest();
        when(passwordHasher.hash("MyPassword123'")).thenReturn("HashedMyPassword123'");

        when(userGateway.existsByEmailAddress(request.emailAddress())).thenReturn(false);
        when(identityProvider.createUser(
                request.firstName(), request.lastName(),
                request.emailAddress(), request.password())).thenReturn(FIREBASE_ID);
        when(passwordHasher.hash(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);

        // Act
        User result = createUser.execute(request);

        // Assert
        assertEquals(FIREBASE_ID, result.getUserId());
        assertEquals("Alessio", result.getFirstName());
        assertEquals("Cocuzza", result.getLastName());
        assertEquals("myemail1@gmail.com", result.getEmailAddress());
        verify(userGateway).createUser(any(User.class), eq(HASHED_PASSWORD));
    }

    @Test
    @DisplayName("Create A User - Empty Inputs")
    void execute_WhenEmptyInputs_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("", "", "", "");

        // Act & Assert
        assertThrows(MissingRequiredFieldException.class, () -> createUser.execute(request));
    }

    @Test
    @DisplayName("Create A User - Null Inputs")
    void execute_WhenNullInputs_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(null, null, null, null);

        // Act & Assert
        assertThrows(MissingRequiredFieldException.class, () -> createUser.execute(request));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidUserData")
    @DisplayName("Create A User - Invalid User Request Data")
    void execute_WhenMissingFirstName_ThrowsException(String displayName, CreateUserRequest request, String expectedErrorMessage) {
        // Act & Assert
        MissingRequiredFieldException exception = assertThrows(MissingRequiredFieldException.class, () -> createUser.execute(request));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Create A User - User Already Exists With Email Address")
    void execute_WhenUserExistsByEmail_ThrowsDuplicatedEmailException() {
        // Arrange
        CreateUserRequest request = provideValidUserRequest();
        when(this.userGateway.existsByEmailAddress(request.emailAddress())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEmailException.class, () -> this.createUser.execute(request));
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Stream<Arguments> invalidUserData() {
        return Stream.of(
                Arguments.of("Missing First Name",
                        new CreateUserRequest(
                                null, "Cocuzza", "myemail1@gmail.com", "MyPassword123'"),
                        "first name cannot be left blank"
                ),
                Arguments.of("Missing Last Name",
                        new CreateUserRequest(
                                "Alessio", null, "myemail1@gmail.com", "MyPassword123'"),
                        "last name cannot be left blank"
                ),
                Arguments.of("Missing Email Address",
                        new CreateUserRequest(
                                "Alessio", "Cocuzza", null, "MyPassword123'"),
                        "email address cannot be left blank"
                ),
                Arguments.of("Missing Password",
                        new CreateUserRequest(
                                "Alessio", "Cocuzza", "myemail1@gmail.com", null),
                        "password cannot be left blank"
                )
        );
    }

    private static CreateUserRequest provideValidUserRequest() {
        return new CreateUserRequest(
                "Alessio", "Cocuzza",
                "myemail1@gmail.com", "MyPassword123'"
        );
    }
}