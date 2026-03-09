package com.gitmadeeasy.usecases.auth;

public interface UserIdentityProvider {
    String createUser(String firstName, String lastName, String email, String password);
    boolean isEmailVerified(String userId);
    String login(String emailAddress, String password);
}