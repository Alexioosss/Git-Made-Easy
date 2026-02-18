package com.gitmadeeasy.usecases.lessons.exceptions;

public class DifficultyLevelNotRecognisedException extends RuntimeException {
    public DifficultyLevelNotRecognisedException(String notFoundDifficulty) {
        super(String.format("difficulty %s not recognised", notFoundDifficulty));
    }
}