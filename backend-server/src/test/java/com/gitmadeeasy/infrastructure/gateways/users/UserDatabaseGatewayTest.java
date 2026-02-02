package com.gitmadeeasy.infrastructure.gateways.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.repositories.UserRepository;
import com.gitmadeeasy.infrastructure.mappers.users.UserSchemaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDatabaseGatewayTest {
    private UserRepository userRepository;
    private UserSchemaMapper userSchemaMapper;
    private UserDatabaseGateway userDatabaseGateway;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userSchemaMapper = mock(UserSchemaMapper.class);
        userDatabaseGateway = new UserDatabaseGateway(userRepository, userSchemaMapper);
    }

    @Test
    @DisplayName("Create User - User Does Not Already Exist")
    void createUser_WhenUserDoesNotExistAlready_ReturnsUser() {
        // Arrange
        User user = provideUser();
        UserSchema userSchema = provideUserSchema();
        when(this.userSchemaMapper.toSchema(user)).thenReturn(userSchema);
        when(this.userRepository.save(userSchema)).thenReturn(userSchema);
        when(this.userSchemaMapper.toEntity(userSchema)).thenReturn(user);

        // Act
        User createdUser = this.userDatabaseGateway.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user.getUserId(), createdUser.getUserId());
        verify(this.userRepository).save(userSchema);
        verify(this.userSchemaMapper).toSchema(user);
        verify(this.userSchemaMapper).toEntity(userSchema);
    }

    @Test
    @DisplayName("Create User - Repository Throws An Exception / Fails")
    void createUser_WhenRepositoryFails_ThrowsException() {
        // Arrange
        User user = provideUser();
        UserSchema userSchema = provideUserSchema();
        when(this.userSchemaMapper.toSchema(user)).thenReturn(userSchema);
        when(this.userRepository.save(userSchema)).thenThrow(new RuntimeException("Database Error."));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.userDatabaseGateway.createUser(user));
    }

    @Test
    @DisplayName("Get User By ID - User Exists / Found")
    void getUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        UserSchema userSchema = provideUserSchema();
        User user = provideUser();
        String userId = "1";
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(userSchema));
        when(this.userSchemaMapper.toEntity(userSchema)).thenReturn(user);

        // Act
        Optional<User> foundUser = this.userDatabaseGateway.getUserById(userId);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
        verify(this.userSchemaMapper).toEntity(userSchema);
    }

    @Test
    @DisplayName("Get User By ID - User Does Not Exist / Not Found")
    void getUserById_WhenUserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        String userId = "1";
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = this.userDatabaseGateway.getUserById(userId);

        // Assert
        assertTrue(foundUser.isEmpty());
        verify(this.userSchemaMapper, never()).toEntity(any(UserSchema.class));
    }

    @Test
    @DisplayName("Get User By Email - User Found")
    void getUserByEmail_WhenUserExists_ReturnsUser() {
        // Arrange
        String email = "test@test.com";
        UserSchema schema = new UserSchema("Alessio", "Cocuzza", email, "HashedMyPassword123'");
        User user = new User("Alessio", "Cocuzza", email, "MyPassword123'");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(schema));
        when(userSchemaMapper.toEntity(schema)).thenReturn(user);

        // Act
        Optional<User> result = userDatabaseGateway.getUserByEmailAddress(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    @DisplayName("Get User By Email - User Not Found")
    void getUserByEmail_WhenUserDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(userRepository.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userDatabaseGateway.getUserByEmailAddress("notfound@gmail.com");

        // Assert
        assertTrue(result.isEmpty());
        verify(userSchemaMapper, never()).toEntity(any());
    }

    @Test
    @DisplayName("Exists By Email - Email Exists")
    void existsByEmail_WhenExists_ReturnsTrue() {
        // Arrange
        when(userRepository.existsByEmail("found@gmail.com")).thenReturn(true);

        // Act
        boolean result = userDatabaseGateway.existsByEmail("found@gmail.com");

        // Assert
        assertTrue(result); verify(userRepository).existsByEmail("found@gmail.com");
    }

    @Test
    @DisplayName("Exists By Email - Email Does Not Exist")
    void existsByEmail_WhenNotExists_ReturnsFalse() {
        // Arrange
        when(userRepository.existsByEmail("notfound@gmail.com")).thenReturn(false);

        // Act
        boolean result = userDatabaseGateway.existsByEmail("notfound@gmail.com");

        // Assert
        assertFalse(result);
        verify(userRepository).existsByEmail("notfound@gmail.com");
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static User provideUser() {
        return new User("Alessio", "Cocuzza", "myemail1@gmail.com", "MyPassword123'");
    }

    private static UserSchema provideUserSchema() {
        return new UserSchema("Alessio", "Cocuzza", "myemail1@gmail.com", "MyPassword123'");
    }
}