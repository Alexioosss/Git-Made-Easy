package com.gitmadeeasy.usecases.lessons.exceptions;

public class LessonDifficultyNotRecognisedException extends RuntimeException {
    public LessonDifficultyNotRecognisedException(String notFoundDifficulty) {
        super(String.format("difficulty %s not recognised", notFoundDifficulty));
    }
}