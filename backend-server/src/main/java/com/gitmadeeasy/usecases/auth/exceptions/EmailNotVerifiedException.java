package com.gitmadeeasy.usecases.auth.exceptions;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException() {
        super("Please verify your email address before continuing.");
    }
}