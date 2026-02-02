package com.gitmadeeasy.usecases.users.exceptions;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String duplicatedEmailAddress) {
        super(String.format("account already exists with email %s", duplicatedEmailAddress));
    }
}