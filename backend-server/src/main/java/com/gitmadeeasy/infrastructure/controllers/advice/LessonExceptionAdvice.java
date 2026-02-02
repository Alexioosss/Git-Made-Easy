package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.lessons.exceptions.LessonDifficultyNotRecognisedException;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LessonExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(LessonNotFoundWithIdException.class)
    public ResponseEntity<ApiError> handleLessonNotFoundWithId(LessonNotFoundWithIdException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "LESSON_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(LessonDifficultyNotRecognisedException.class)
    public ResponseEntity<ApiError> handleLessonDifficultyNotRecognised(LessonDifficultyNotRecognisedException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "LESSON_NOT_FOUND", ex.getMessage());
    }
}