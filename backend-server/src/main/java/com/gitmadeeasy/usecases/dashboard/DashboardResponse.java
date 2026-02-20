package com.gitmadeeasy.usecases.dashboard;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;

import java.util.List;

public record DashboardResponse(
        String userId, String firstName, String lastName,
        List<LessonProgress> lessonsProgress,
        List<TaskProgress> tasksProgress) {}