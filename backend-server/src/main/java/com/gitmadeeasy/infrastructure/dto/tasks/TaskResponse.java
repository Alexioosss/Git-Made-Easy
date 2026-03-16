package com.gitmadeeasy.infrastructure.dto.tasks;

public record TaskResponse(String taskId, String lessonId, String title, String content, String expectedCommand,
                           String hint, Integer taskOrder, String taskDifficulty) {}