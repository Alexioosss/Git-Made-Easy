package com.gitmadeeasy.usecases.auth;

public interface UserIdentityProvider {
    String createUser(String firstName, String lastName, String email, String password);
    String generateEmailVerificationLink(String email);
    boolean isEmailVerified(String userId);
}