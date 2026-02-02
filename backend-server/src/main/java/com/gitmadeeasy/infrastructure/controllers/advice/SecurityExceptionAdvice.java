package com.gitmadeeasy.infrastructure.controllers.advice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionAdvice extends BaseErrorAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token is invalid or expired.");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ApiError> handleCredentialsExpired(CredentialsExpiredException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "Token has expired.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "EXPIRED_TOKEN", "JWT Token has expired.");
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleInvalidJwt(JwtException ex) {
        return this.buildError(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Token is invalid.");
    }
}