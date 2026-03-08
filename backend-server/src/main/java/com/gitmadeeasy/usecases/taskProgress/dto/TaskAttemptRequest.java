package com.gitmadeeasy.usecases.taskProgress.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskAttemptRequest(@NotBlank String input) {}