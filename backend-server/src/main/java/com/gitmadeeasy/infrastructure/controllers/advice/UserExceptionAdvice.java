package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.users.exceptions.InvalidUserDataException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUserData(InvalidUserDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError("INVALID_USER_DATA", ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UserNotFoundWithIdException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundWithId(UserNotFoundWithIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError("USER_NOT_FOUND_BY_ID", ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UserNotFoundWithEmailException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundWithEmail(UserNotFoundWithEmailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError("USER_NOT_FOUND_BY_EMAIL", ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    private Map<String, Object> buildError(String errorCode, String errorMessage, HttpStatus httpStatus) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", httpStatus.value(),
                "errorCode", errorCode,
                "message", errorMessage
        );
    }
}