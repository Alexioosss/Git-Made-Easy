package com.gitmadeeasy.usecases.taskProgress.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskProgressUpdateRequest(
        @NotBlank String taskId,
        String status,
        @NotNull Integer attempts,
        String lastInput,
        String lastError,
        String startedAt,
        String completedAt) {}