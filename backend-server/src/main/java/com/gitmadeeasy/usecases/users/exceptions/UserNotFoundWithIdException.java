package com.gitmadeeasy.usecases.users.exceptions;

public class UserNotFoundWithIdException extends RuntimeException {
    public UserNotFoundWithIdException(String userId) {
        super(String.format("user %s not found", userId));
    }
}