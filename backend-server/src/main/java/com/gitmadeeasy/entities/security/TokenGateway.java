package com.gitmadeeasy.entities.security;

import com.gitmadeeasy.entities.users.User;

public interface TokenGateway {
    String generateToken(User user);
    void invalidateToken(String token);
    String refreshToken(User user);
    boolean isTokenValid(String token);
    String getUserIdFromToken(String token);
}