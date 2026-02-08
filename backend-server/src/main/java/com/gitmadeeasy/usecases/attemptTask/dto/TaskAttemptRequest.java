package com.gitmadeeasy.usecases.attemptTask.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskAttemptRequest(@NotBlank String input) {}