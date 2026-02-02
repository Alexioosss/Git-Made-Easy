package com.gitmadeeasy.usecases.users.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("invalid email or password");
    }
}