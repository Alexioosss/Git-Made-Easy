package com.gitmadeeasy.usecases.taskAttempt.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskAttemptRequest(@NotBlank String input) {}