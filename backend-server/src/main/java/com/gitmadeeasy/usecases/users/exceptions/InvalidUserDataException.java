package com.gitmadeeasy.usecases.users.exceptions;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String invalidField) {
        super(String.format("%s Cannot Be Left Blank.", invalidField));
    }
}