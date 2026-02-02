package com.gitmadeeasy.usecases.lessons.exceptions;

public class LessonNotFoundWithIdException extends RuntimeException {
    public LessonNotFoundWithIdException(String lessonId) {
        super(String.format("Lesson With ID %s Not Found.", lessonId));
    }
}