package com.gitmadeeasy.usecases.users.exceptions;

public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException() {
        super("The email address has already been verified.");
    }
}