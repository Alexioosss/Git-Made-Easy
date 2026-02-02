package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
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
}