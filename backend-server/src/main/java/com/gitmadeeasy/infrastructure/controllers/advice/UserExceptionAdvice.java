package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.users.exceptions.DuplicatedEmailException;
import com.gitmadeeasy.usecases.users.exceptions.MissingRequiredFieldException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<ApiError> handleInvalidUserData(MissingRequiredFieldException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "INVALID_USER_DATA", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundWithIdException.class)
    public ResponseEntity<ApiError> handleUserNotFoundWithId(UserNotFoundWithIdException ex) {
        return buildError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND_BY_ID", ex.getMessage());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ApiError> handleDuplicatedEmail(DuplicatedEmailException ex) {
        return buildError(HttpStatus.CONFLICT, "DUPLICATED_EMAIL", ex.getMessage());
    }

    private ResponseEntity<ApiError> buildError(HttpStatus statusCode, String code, String message) {
        return ResponseEntity.status(statusCode).body(
                ApiError.buildError(code, message, statusCode.value())
        );
    }
}