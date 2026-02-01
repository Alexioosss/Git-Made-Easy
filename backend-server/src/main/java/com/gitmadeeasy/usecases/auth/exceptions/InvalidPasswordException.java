package com.gitmadeeasy.usecases.auth.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("The Password Entered Is Invalid. Please Try Again.");
    }
}