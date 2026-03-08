package com.gitmadeeasy.infrastructure.controllers.advice;

import com.gitmadeeasy.usecases.auth.exceptions.EmailNotVerifiedException;
import com.gitmadeeasy.usecases.auth.exceptions.InvalidTokenException;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return this.buildError(HttpStatus.BAD_REQUEST, "ILLEGAL_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "email or password is incorrect");
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", ex.getMessage());
    }

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<ApiError> handleInvalidUserData(MissingRequiredFieldException ex) {
        return this.buildError(HttpStatus.BAD_REQUEST, "MISSING_REQUIRED_FIELD", ex.getMessage());
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ApiError> handleEmailNotVerified(EmailNotVerifiedException ex) {
        return this.buildError(HttpStatus.FORBIDDEN, "EMAIL_NOT_VERIFIED", ex.getMessage());
    }
}