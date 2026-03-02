package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;

public class FirebaseTaskAttemptSchema {
    private String id;
    private String lessonId;
    private String taskId;
    private String userId;
    private String taskTitle;
    private TaskCompletionStatus status;
    private int attempts;
    private String lastInput;
    private String lastError;
    private String startedAt;
    private String completedAt;

    protected FirebaseTaskAttemptSchema() {}

    public FirebaseTaskAttemptSchema(
            String userId, String taskId, TaskCompletionStatus status, int attempts,
            String lastInput, String lastError, String startedAt, String completedAt) {
        this.userId = userId;
        this.taskId = taskId;
        this.status = status;
        this.attempts = attempts;
        this.lastInput = lastInput;
        this.lastError = lastError;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public String getLessonId() { return lessonId; }

    public String getTaskId() { return taskId; }

    public String getUserId() { return userId; }

    public String getTaskTitle() { return taskTitle; }

    public TaskCompletionStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastInput() {
        return lastInput;
    }

    public String getLastError() {
        return lastError;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setId(String id) { this.id = id; }

    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    public void setTaskTitle(String taskTitle) { this.taskTitle = taskTitle; }
}