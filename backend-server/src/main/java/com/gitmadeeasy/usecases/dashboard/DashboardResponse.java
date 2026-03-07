package com.gitmadeeasy.usecases.dashboard;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;

import java.util.List;

public record DashboardResponse(String userId, String firstName, String lastName,
                                List<LessonSummary> lessons, List<TaskProgress> tasksProgress) {}