package com.gitmadeeasy.usecases.users.exceptions;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String duplicatedEmailAddress) {
        super(String.format("An Account Already Exists With The Email Address %s", duplicatedEmailAddress));
    }
}