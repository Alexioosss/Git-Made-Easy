package com.gitmadeeasy.usecases.attemptTask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskProgressUpdateRequest(
        @NotBlank String taskId,
        @NotBlank String answer,
        String status,
        @NotNull Integer attempts,
        String lastInput,
        String lastError,
        String startedAt,
        String completedAt) {}