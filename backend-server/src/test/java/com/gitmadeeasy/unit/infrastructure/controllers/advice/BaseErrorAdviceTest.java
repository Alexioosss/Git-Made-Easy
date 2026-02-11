package com.gitmadeeasy.unit.infrastructure.controllers.advice;

import com.gitmadeeasy.infrastructure.controllers.advice.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseErrorAdviceTest {

    @Test
    @DisplayName("Build Error - Returns Api Error With Given Values")
    void buildError_WhenGivenValues_ReturnsApiErrorWithCorrectValues() {
        // Arrange & Act
        ApiError error = ApiError.buildError("TASK_NOT_FOUND", "Task does not exist", 404);

        // Assert
        assertEquals(404, error.status());
        assertEquals("TASK_NOT_FOUND", error.errorCode());
        assertEquals("Task does not exist", error.message());
        assertDoesNotThrow(() -> Instant.parse(error.timestamp()));
    }
}