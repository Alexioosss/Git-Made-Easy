package com.gitmadeeasy.usecases.tasks.dto;

public record CreateTaskRequest(
        String title, String content,
        String expectedCommand, String hint) {}