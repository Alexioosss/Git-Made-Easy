package com.gitmadeeasy.usecases.users.exceptions;

public class UserNotFoundWithEmailException extends RuntimeException {
    public UserNotFoundWithEmailException(String emailAddress) {
        super(String.format("user not found with email %s", emailAddress));
    }
}