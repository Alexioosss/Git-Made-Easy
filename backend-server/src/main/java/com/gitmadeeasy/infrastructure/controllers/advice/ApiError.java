package com.gitmadeeasy.infrastructure.controllers.advice;

import java.time.Instant;

public record ApiError(String timestamp, int status, String errorCode, String message) {

    public static ApiError buildError(String code, String message, int status) {
        return new ApiError(Instant.now().toString(), status, code, message);
    }
}