package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

public class LessonProgressSchema {

    private String id;
    private String userId;
    private String lessonId;
    private String currentTaskProgressId;
    private Integer completedTasksCount;
    private Integer totalTasksCount;

    public LessonProgressSchema() {}

    public LessonProgressSchema(String userId, String lessonId, String currentTaskProgressId,
                                Integer completedTasksCount, Integer totalTasksCount) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.currentTaskProgressId = currentTaskProgressId;
        this.completedTasksCount = completedTasksCount;
        this.totalTasksCount = totalTasksCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
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