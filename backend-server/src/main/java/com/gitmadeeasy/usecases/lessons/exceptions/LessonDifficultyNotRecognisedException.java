package com.gitmadeeasy.usecases.lessons.exceptions;

public class LessonDifficultyNotRecognisedException extends RuntimeException {
    public LessonDifficultyNotRecognisedException(String notFoundDifficulty) {
        super(String.format("Difficulty %s Not Found. Please Try Again.", notFoundDifficulty));
    }
}