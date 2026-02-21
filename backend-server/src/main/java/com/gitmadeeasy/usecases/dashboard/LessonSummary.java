package com.gitmadeeasy.usecases.dashboard;

public record LessonSummary(String lessonId, String title, Integer lessonOrder,
                            Integer completedTasksCount, Integer totalTasksCount) {}