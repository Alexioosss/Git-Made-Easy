package com.gitmadeeasy.unit.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.JpaUserDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.users.JpaUserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.JpaUserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserDatabaseGatewayTest {
    @Mock private JpaUserRepository jpaUserRepository;
    @Mock private UserSchemaMapper userSchemaMapper;
    @InjectMocks private JpaUserDatabaseGateway jpaUserDatabaseGateway;


    @Test
    @DisplayName("Create User - Valid User Is Saved And Returned")
    void createUser_WhenUserDoesNotExistAlready_ReturnsUser() {
        // Arrange
        User user = provideUserWithId();
        JpaUserSchema userSchema = provideUserSchemaWithId();
        when(this.userSchemaMapper.toJpaSchema(user)).thenReturn(userSchema);
        when(this.jpaUserRepository.save(userSchema)).thenReturn(userSchema);
        when(this.userSchemaMapper.fromJpaSchema(userSchema)).thenReturn(user);

        // Act
        User createdUser = this.jpaUserDatabaseGateway.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user.getUserId(), createdUser.getUserId());
        verify(this.jpaUserRepository).save(userSchema);
        verify(this.userSchemaMapper).toJpaSchema(user);
        verify(this.userSchemaMapper).fromJpaSchema(userSchema);
    }

    @Test
    @DisplayName("Create User - Repository Throws An Exception / Fails")
    void createUser_WhenRepositoryFails_ThrowsException() {
        // Arrange
        User user = provideUserWithId();
        JpaUserSchema userSchema = provideUserSchemaWithId();
        when(this.userSchemaMapper.toJpaSchema(user)).thenReturn(userSchema);
        when(this.jpaUserRepository.save(userSchema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.jpaUserDatabaseGateway.createUser(user));
    }

    @Test
    @DisplayName("Get User By ID - User Exists / Found")
    void getUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        JpaUserSchema userSchema = provideUserSchemaWithId();
        User user = provideUserWithId();
        final String userId = "1";
        when(this.jpaUserRepository.findById(userId)).thenReturn(Optional.of(userSchema));
        when(this.userSchemaMapper.fromJpaSchema(userSchema)).thenReturn(user);

        // Act
        Optional<User> foundUser = this.jpaUserDatabaseGateway.getUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(this.userSchemaMapper).fromJpaSchema(userSchema);
    }

    @Test
    @DisplayName("Get User By ID - User Does Not Exist / Not Found")
    void getUserById_WhenUserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        final String userId = "1";
        when(this.jpaUserRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = this.jpaUserDatabaseGateway.getUserById(userId);

        // Assert
        assertTrue(foundUser.isEmpty());
        verify(this.userSchemaMapper, never()).fromJpaSchema(any(JpaUserSchema.class));
    }

    @Test
    @DisplayName("Get User By Email - User Found")
    void getUserByEmail_WhenUserExists_ReturnsUser() {
        // Arrange
        final String email = "test@test.com";
        JpaUserSchema schema = new JpaUserSchema("1", "Alessio", "Cocuzza", email, false);
        User user = new User("Alessio", "Cocuzza", email);
        when(jpaUserRepository.findByEmailAddress(email)).thenReturn(Optional.of(schema));
        when(userSchemaMapper.fromJpaSchema(schema)).thenReturn(user);

        // Act
        Optional<User> result = jpaUserDatabaseGateway.getUserByEmailAddress(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Get User By Email - User Not Found")
    void getUserByEmail_WhenUserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        final String email = "notfound@gmail.com";
        when(jpaUserRepository.findByEmailAddress(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = jpaUserDatabaseGateway.getUserByEmailAddress(email);

        // Assert
        assertTrue(result.isEmpty());
        verify(userSchemaMapper, never()).fromJpaSchema(any());
    }

    @Test
    @DisplayName("Exists By Email - Email Exists")
    void existsByEmailAddress_WhenUserExists_ReturnsTrueAddress() {
        // Arrange
        final String email = "found@gmail.com";
        when(jpaUserRepository.existsByEmailAddress(email)).thenReturn(true);

        // Act
        boolean exists = jpaUserDatabaseGateway.existsByEmailAddress(email);

        // Assert
        assertTrue(exists); verify(jpaUserRepository).existsByEmailAddress(email);
    }

    @Test
    @DisplayName("Exists By Email - Email Does Not Exist")
    void existsByEmailAddress_WhenUserDoesNotExist_ReturnsFalseAddress() {
        // Arrange
        final String email = "notfound@gmail.com";
        when(jpaUserRepository.existsByEmailAddress(email)).thenReturn(false);

        // Act
        boolean exists = jpaUserDatabaseGateway.existsByEmailAddress(email);

        // Assert
        assertFalse(exists);
        verify(jpaUserRepository).existsByEmailAddress(email);
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static User provideUserWithId() {
        return new User("1", "John", "Doe", "myemail1@gmail.com", false);
    }

    private static JpaUserSchema provideUserSchemaWithId() {
        return new JpaUserSchema("1", "John", "Doe", "myemail1@gmail.com", false);
    }
}