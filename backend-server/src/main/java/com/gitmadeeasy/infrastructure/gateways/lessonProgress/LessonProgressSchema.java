package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "lesson_progress")
public class LessonProgressSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonProgressId;

    private Long userId;
    private Long lessonId;
    private Long currentTaskProgressId;
    private Integer completedTasksCount;
    private Integer totalTasksCount;

    protected LessonProgressSchema() {}

    public LessonProgressSchema(String userId, String lessonId, String currentTaskProgressId,
                                Integer completedTasksCount, Integer totalTasksCount) {
        this.userId = Long.valueOf(userId);
        this.lessonId = Long.valueOf(lessonId);
        this.currentTaskProgressId = Long.valueOf(currentTaskProgressId);
        this.completedTasksCount = completedTasksCount;
        this.totalTasksCount = totalTasksCount;
    }

    public String getLessonProgressId() {
        return String.valueOf(lessonProgressId);
    }

    public String getUserId() {
        return String.valueOf(userId);
    }

    public String getLessonId() {
        return String.valueOf(lessonId);
    }

    public String getCurrentTaskProgressId() {
        return String.valueOf(currentTaskProgressId);
    }

    public Integer getCompletedTasksCount() {
        return completedTasksCount;
    }

    public Integer getTotalTasksCount() {
        return totalTasksCount;
    }

    public void setLessonProgressId(Long lessonProgressId) {
        this.lessonProgressId = lessonProgressId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public void setCurrentTaskProgressId(Long currentTaskProgressId) {
        this.currentTaskProgressId = currentTaskProgressId;
    }

    public void setCompletedTasksCount(Integer completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public void setTotalTasksCount(Integer totalTasksCount) {
        this.totalTasksCount = totalTasksCount;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        LessonProgressSchema that = (LessonProgressSchema) o;
        return Objects.equals(lessonProgressId, that.lessonProgressId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lessonProgressId);
    }
}