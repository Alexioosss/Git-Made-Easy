package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity @Table(name = "task_attempts")
public class JpaTaskAttemptSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private JpaTaskSchema task;

    @Enumerated(EnumType.STRING) private TaskCompletionStatus status;

    private int attempts;
    private String lastInput;
    private String lastError;
    private LocalDate startedAt;
    private LocalDate completedAt;

    protected JpaTaskAttemptSchema() {}

    public JpaTaskAttemptSchema(
            String userId, TaskCompletionStatus status, int attempts,
            String lastInput, String lastError, LocalDate startedAt, LocalDate completedAt, JpaTaskSchema task) {
        this.userId = userId;
        this.status = status;
        this.attempts = attempts;
        this.lastInput = lastInput;
        this.lastError = lastError;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public JpaTaskSchema getTask() {
        return task;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTask(JpaTaskSchema task) {
        this.task = task;
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