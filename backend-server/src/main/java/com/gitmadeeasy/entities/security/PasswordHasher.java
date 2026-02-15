package com.gitmadeeasy.entities.security;

public interface PasswordHasher {
    String hash(String raw);
    boolean matches(String rawPassword, String hashedPassword);
}