package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "task_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "taskId"}))
public class TaskAttemptSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskProgressId;

    private Long userId;
    private Long taskId;
    @Enumerated(EnumType.STRING)
    private TaskCompletionStatus status;
    private int attempts;
    private String lastInput;
    private String lastError;
    private LocalDate startedAt;
    private LocalDate completedAt;

    protected TaskAttemptSchema() {
    }

    public TaskAttemptSchema(
            String userId, String taskId, TaskCompletionStatus status, int attempts,
            String lastInput, String lastError, LocalDate startedAt, LocalDate completedAt) {
        this.userId = Long.valueOf(userId);
        this.taskId = Long.valueOf(taskId);
        this.status = status;
        this.attempts = attempts;
        this.lastInput = lastInput;
        this.lastError = lastError;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public String getTaskProgressId() {
        return String.valueOf(taskProgressId);
    }

    public String getUserId() {
        return String.valueOf(userId);
    }

    public String getTaskId() {
        return String.valueOf(taskId);
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

    public void setTaskProgressId(Long taskProgressId) {
        this.taskProgressId = taskProgressId;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        TaskAttemptSchema that = (TaskAttemptSchema) o;
        return Objects.equals(taskProgressId, that.taskProgressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskProgressId);
    }
}