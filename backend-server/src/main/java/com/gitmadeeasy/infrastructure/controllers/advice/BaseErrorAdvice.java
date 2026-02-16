package com.gitmadeeasy.infrastructure.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract Class to act as an adapter between application / use-case exceptions and HTTP responses
 * Belongs in the infrastructure layer since it deals with the outside world, and with infrastructural concerns.
 */
public abstract class BaseErrorAdvice {
    protected ResponseEntity<ApiError> buildError(HttpStatus httpStatus, String code, String message) {
        return ResponseEntity.status(httpStatus).body(ApiError.buildError(code, message, httpStatus.value()));
    }
}