package com.gitmadeeasy.infrastructure.controllers.advice;

import java.time.Instant;

/**
 * Custom Record class to be used as the public presenter for errors thrown / sent by the api
 * @param timestamp the time of when the error occurred
 * @param status the HTTP status code (i.e. 400, 401, 404, 500 etc.)
 * @param errorCode a custom error code to give more meaning to the error (i.e. INVALID_CREDENTIALS, MISSING_REQUIRED_FIELD)
 * @param message the error message to be displayed for further clarity and guidance
 */
public record ApiError(String timestamp, int status, String errorCode, String message) {
    public static ApiError buildError(String code, String message, int status) {
        return new ApiError(Instant.now().toString(), status, code, message);
    }
}