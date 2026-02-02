package com.gitmadeeasy.usecases.lessons.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLessonRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String difficulty) {}