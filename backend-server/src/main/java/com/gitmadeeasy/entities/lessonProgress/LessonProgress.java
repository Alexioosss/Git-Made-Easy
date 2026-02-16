package com.gitmadeeasy.entities.lessonProgress;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "LessonProgress{" +
                "lessonProgressId='" + lessonProgressId + '\'' +
                ", userId='" + userId + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", currentTaskProgressId='" + currentTaskProgressId + '\'' +
                ", completedTasksCount=" + completedTasksCount +
                ", totalTasksCount=" + totalTasksCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        LessonProgress that = (LessonProgress) o;
        return Objects.equals(lessonProgressId, that.lessonProgressId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(currentTaskProgressId, that.currentTaskProgressId) &&
                Objects.equals(completedTasksCount, that.completedTasksCount) &&
                Objects.equals(totalTasksCount, that.totalTasksCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonProgressId, userId, lessonId, currentTaskProgressId, completedTasksCount, totalTasksCount);
    }
}