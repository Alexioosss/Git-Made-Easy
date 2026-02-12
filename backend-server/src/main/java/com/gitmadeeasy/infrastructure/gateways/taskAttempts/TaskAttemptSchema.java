package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import java.time.LocalDate;

public class TaskAttemptSchema {

    private String id;
    private String userId;
    private String taskId;
    private TaskCompletionStatus status;
    private int attempts;
    private String lastInput;
    private String lastError;
    private LocalDate startedAt;
    private LocalDate completedAt;

    public TaskAttemptSchema() {
    }

    public TaskAttemptSchema(
            String userId, String taskId, TaskCompletionStatus status, int attempts,
            String lastInput, String lastError, LocalDate startedAt, LocalDate completedAt) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTaskId() {
        return taskId;
    }

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

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setStatus(TaskCompletionStatus status) {
        this.status = status;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setLastInput(String lastInput) {
        this.lastInput = lastInput;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }
}