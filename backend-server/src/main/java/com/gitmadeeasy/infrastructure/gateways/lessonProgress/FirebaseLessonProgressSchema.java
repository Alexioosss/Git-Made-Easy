package com.gitmadeeasy.infrastructure.gateways.lessonProgress;

import java.util.Objects;

public class FirebaseLessonProgressSchema {
    private String id;
    private String userId;
    private String lessonId;
    private String currentTaskProgressId;
    private Integer completedTasksCount;
    private Integer totalTasksCount;

    protected FirebaseLessonProgressSchema() {}

    public FirebaseLessonProgressSchema(String userId, String lessonId, String currentTaskProgressId,
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

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        FirebaseLessonProgressSchema that = (FirebaseLessonProgressSchema) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}