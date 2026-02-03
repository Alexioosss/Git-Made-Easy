package com.gitmadeeasy.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {
    private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    @DisplayName("Hash - Valid Password Is Hashed And Returned")
    void hash_WhenProvidedAPassword_ReturnsHashedPassword() {
        // Arrange
        String rawPassword = "MySecurePassword123";

        // Act
        String hash1 = hasher.hash(rawPassword);
        String hash2 = hasher.hash(rawPassword);

        // Assert
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(rawPassword, hash1);
        assertNotEquals(rawPassword, hash2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Matches - Matching Passwords Returns True")
    void matches_WhenPasswordsAreCorrect_ShouldReturnTrue() {
        // Arrange
        String rawPassword = "MySecurePassword123";

        // Act
        String hashed = hasher.hash(rawPassword);

        // Assert
        assertTrue(hasher.matches(rawPassword, hashed));
    }

    @Test
    @DisplayName("Matches - Mismatching Passwords Returns False")
    void matches_WhenPasswordsAreIncorrect_ShouldReturnFalse() {
        // Arrange
        String rawPassword = "MySecurePassword123";

        // Act
        String hashed = hasher.hash(rawPassword);

        // Assert
        assertFalse(hasher.matches("MySecurePassword", hashed));
    }
}