package com.gitmadeeasy.usecases.lessons.exceptions;

public class LessonNotFoundWithIdException extends RuntimeException {
    public LessonNotFoundWithIdException(String lessonId) {
        super(String.format("lesson %s not found", lessonId));
    }
}