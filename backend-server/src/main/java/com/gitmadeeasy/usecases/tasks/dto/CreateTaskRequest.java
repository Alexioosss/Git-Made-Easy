package com.gitmadeeasy.usecases.tasks.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String expectedCommand,
        String hint,
        @Nullable Integer taskOrder) {}