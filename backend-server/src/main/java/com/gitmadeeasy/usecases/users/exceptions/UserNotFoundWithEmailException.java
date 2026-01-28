package com.gitmadeeasy.usecases.users.exceptions;

public class UserNotFoundWithEmailException extends RuntimeException {
    public UserNotFoundWithEmailException(String emailAddress) {
        super(String.format("User Not Found With Email Address: %s.", emailAddress));
    }
}