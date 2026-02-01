package com.gitmadeeasy.entities.security;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String newPassword, String storedPassword);
}