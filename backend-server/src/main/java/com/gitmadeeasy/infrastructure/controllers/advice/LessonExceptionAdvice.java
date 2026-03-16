package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.lessons.exceptions.DifficultyLevelNotRecognisedException;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LessonExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(LessonNotFoundWithIdException.class)
    public ResponseEntity<ApiError> handleLessonNotFoundWithId(LessonNotFoundWithIdException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "LESSON_NOT_FOUND_BY_ID", ex.getMessage());
    }

    @ExceptionHandler(DifficultyLevelNotRecognisedException.class)
    public ResponseEntity<ApiError> handleLessonDifficultyNotRecognised(DifficultyLevelNotRecognisedException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "DIFFICULTY_NOT_RECOGNISED", ex.getMessage());
    }
}