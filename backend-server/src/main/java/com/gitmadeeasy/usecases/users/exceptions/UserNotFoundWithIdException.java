package com.gitmadeeasy.usecases.users.exceptions;

public class UserNotFoundWithIdException extends RuntimeException {
    public UserNotFoundWithIdException(String userId) {
        super(String.format("User Not Found With ID %s.", userId));
    }
}