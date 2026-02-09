package com.gitmadeeasy.unit.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHasherTest {
    private static final String RAW_PASSWORD = "MySecurePassword123'";
    private final BCryptPasswordHasher hasher = new BCryptPasswordHasher();

    @Test
    @DisplayName("Hash - Valid Password Is Hashed And Returned")
    void hash_WhenProvidedAPassword_ReturnsHashedPassword() {
        // Act
        String hash1 = hasher.hash(RAW_PASSWORD);
        String hash2 = hasher.hash(RAW_PASSWORD);

        // Assert
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(RAW_PASSWORD, hash1);
        assertNotEquals(RAW_PASSWORD, hash2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("Matches - Matching Passwords Returns True")
    void matches_WhenPasswordsAreCorrect_ShouldReturnTrue() {
        // Act
        String hashed = hasher.hash(RAW_PASSWORD);

        // Assert
        assertTrue(hasher.matches(RAW_PASSWORD, hashed));
    }

    @Test
    @DisplayName("Matches - Mismatching Passwords Returns False")
    void matches_WhenPasswordsAreIncorrect_ShouldReturnFalse() {
        // Act
        String hashed = hasher.hash(RAW_PASSWORD);

        // Assert
        assertFalse(hasher.matches("MySecurePassword", hashed));
    }
}