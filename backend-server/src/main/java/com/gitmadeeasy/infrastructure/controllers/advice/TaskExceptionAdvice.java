package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotInLessonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(TaskNotFoundWithIdException.class)
    public ResponseEntity<ApiError> handleTaskNotFoundWithId(TaskNotFoundWithIdException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(TaskNotInLessonException.class)
    public ResponseEntity<ApiError> handleTaskNotInLesson(TaskNotInLessonException ex) {
        return this.buildError(HttpStatus.BAD_REQUEST, "TASK_NOT_IN_LESSON", ex.getMessage());
    }
}