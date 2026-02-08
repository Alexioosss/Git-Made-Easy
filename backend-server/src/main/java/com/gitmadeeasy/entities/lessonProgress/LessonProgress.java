package com.gitmadeeasy.entities.lessonProgress;

public class LessonProgress {
    private String lessonProgressId;
    private String userId;
    private String lessonId;
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

    public void setLessonProgressId(String lessonProgressId) {
        this.lessonProgressId = lessonProgressId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setCurrentTaskProgressId(String currentTaskProgressId) {
        this.currentTaskProgressId = currentTaskProgressId;
    }

    public void setCompletedTasksCount(Integer completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public void setTotalTasksCount(Integer totalTasksCount) {
        this.totalTasksCount = totalTasksCount;
    }
}