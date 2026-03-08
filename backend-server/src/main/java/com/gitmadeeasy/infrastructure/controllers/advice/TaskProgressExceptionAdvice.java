package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.taskProgress.exceptions.TaskProgressNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskProgressExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(TaskProgressNotFoundException.class)
    public ResponseEntity<ApiError> handleTaskProgressNotFound(TaskProgressNotFoundException ex) {
        return this.buildError(HttpStatus.NOT_FOUND, "TASK_PROGRESS_NOT_FOUND", ex.getMessage());
    }
}