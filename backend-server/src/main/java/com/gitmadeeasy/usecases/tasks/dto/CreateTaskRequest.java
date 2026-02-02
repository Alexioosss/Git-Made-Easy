package com.gitmadeeasy.usecases.tasks.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String expectedCommand,
        String hint) {}