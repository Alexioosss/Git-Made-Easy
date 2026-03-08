package com.gitmadeeasy.usecases.shared.exceptions;

public class MissingRequiredFieldException extends RuntimeException {
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}