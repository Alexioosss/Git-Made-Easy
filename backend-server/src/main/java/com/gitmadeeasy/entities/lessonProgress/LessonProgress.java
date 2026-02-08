package com.gitmadeeasy.entities.lessonProgress;

public class LessonProgress {
    private String lessonProgressId;
    private final String userId;
    private final String lessonId;
    private String currentTaskProgressId;
    private Integer completedTasksCount;
    private Integer totalTasksCount;

    public LessonProgress(String lessonProgressId, String userId, String lessonId,
                          String currentTaskProgressId, Integer completedTasksCount,
                          Integer totalTasksCount) {
        this.lessonProgressId = lessonProgressId;
        this.userId = userId;
        this.lessonId = lessonId;
        this.currentTaskProgressId = currentTaskProgressId;
        this.completedTasksCount = completedTasksCount;
        this.totalTasksCount = totalTasksCount;
    }

    public String getUserId() {
        return userId;
    }

    public String getLessonProgressId() {
        return lessonProgressId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getCurrentTaskProgressId() {
        return currentTaskProgressId;
    }

    public Integer getCompletedTasksCount() {
        return completedTasksCount;
    }

    public Integer getTotalTasksCount() {
        return totalTasksCount;
    }

    public void updateTasksCounts(int completedTasks, int totalTasks) {
        if(completedTasks < 0 || totalTasks < 0) {
            throw new IllegalArgumentException("Task counts cannot be negative");
        }
        if(completedTasks > totalTasks) {
            throw new IllegalArgumentException("Completed tasks cannot exceed the total tasks");
        }

        this.completedTasksCount = completedTasks;
        this.totalTasksCount = totalTasks;
    }

    public void setLessonProgressId(String lessonProgressId) {
        this.lessonProgressId = lessonProgressId;
    }

    public void setCurrentTaskProgressId(String currentTaskProgressId) {
        this.currentTaskProgressId = currentTaskProgressId;
    }
}