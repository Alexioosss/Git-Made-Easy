package com.gitmadeeasy.unit.usecases.auth;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.auth.ResendVerificationEmail;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.email.VerificationEmailService;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import com.gitmadeeasy.usecases.users.exceptions.EmailAlreadyVerifiedException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResendVerificationEmailTest {
    @Mock private UserGateway userGateway;
    @Mock private UserIdentityProvider userIdentityProvider;
    @Mock private VerificationEmailService verificationEmailService;
    @InjectMocks private ResendVerificationEmail resendVerificationEmail;


    @Test
    @DisplayName("Execute - Sends Verification Email When User Exists And Email Not Verified")
    void execute_WhenUserExistsAndEmailNotVerified_SendsVerificationEmail() {
        String email = "myemail1@gmail.com";
        User user = new User("1", "John", "Doe", email);
        when(this.userGateway.getUserByEmailAddress(email)).thenReturn(java.util.Optional.of(user));
        when(this.userIdentityProvider.isEmailVerified(user.getUserId())).thenReturn(false);

        // Act
        this.resendVerificationEmail.execute(email);

        // Assert
        verify(this.verificationEmailService).sendVerificationEmail(email, user.getFirstName());
    }

    @Test
    @DisplayName("Execute - Throws When Email Is Null")
    void execute_WhenEmailIsNull_ThrowsMissingRequiredFieldException() {
        // Act & Assert
        assertThrows(MissingRequiredFieldException.class, () -> this.resendVerificationEmail.execute(null));
    }

    @Test
    @DisplayName("Execute - Throws When Email Is Blank")
    void execute_WhenEmailIsBlank_ThrowsMissingRequiredFieldException() {
        // Act & Assert
        assertThrows(MissingRequiredFieldException.class, () -> this.resendVerificationEmail.execute("   "));
    }

    @Test
    @DisplayName("Execute - Throws When User Not Found")
    void execute_ShouldThrow_WhenUserNotFound() {
        // Arrange
        String email = "myemail1@gmail.com";
        when(this.userGateway.getUserByEmailAddress(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundWithEmailException.class, () -> this.resendVerificationEmail.execute(email));
    }

    @Test
    @DisplayName("Execute - Throws When Email Already Verified")
    void execute_WhenEmailAlreadyVerified_ThrowsEmailAlreadyVerifiedException() {
        // Arrange
        String email = "myemail1@gmail.com";
        User user = new User("1", "John", "Doe", email);
        when(this.userGateway.getUserByEmailAddress(email)).thenReturn(java.util.Optional.of(user));
        when(this.userIdentityProvider.isEmailVerified(user.getUserId())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyVerifiedException.class, () -> this.resendVerificationEmail.execute(email));
    }
}