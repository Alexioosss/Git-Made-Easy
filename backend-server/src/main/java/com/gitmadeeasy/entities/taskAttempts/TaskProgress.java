package com.gitmadeeasy.entities.taskAttempts;

import com.gitmadeeasy.entities.tasks.Task;

import java.time.LocalDate;

public class TaskProgress {
    private final String taskProgressId;
    private final String userId;
    private final String taskId;
    private TaskCompletionStatus status;
    private int attempts;
    private String lastInput;
    private String lastError;
    private LocalDate startedAt;
    private LocalDate completedAt;

    public TaskProgress(String taskProgressId, String userId, String taskId) {
        this.taskProgressId = taskProgressId;
        this.userId = userId;
        this.taskId = taskId;
        this.status = TaskCompletionStatus.NOT_STARTED;
        this.attempts = 0;
        this.startedAt = LocalDate.now();
    }

    public TaskProgress(String taskProgressId, String userId, String taskId, TaskCompletionStatus status,
                        int attempts, String lastInput, String lastError,
                        LocalDate startedAt, LocalDate completedAt) {
        this.taskProgressId = taskProgressId;
        this.userId = userId;
        this.taskId = taskId;
        this.status = status;
        this.attempts = attempts;
        this.lastInput = lastInput;
        this.lastError = lastError;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public String getTaskProgressId() {
        return taskProgressId;
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

    public boolean isCompleted() {
        return this.status == TaskCompletionStatus.COMPLETED;
    }

    public void attempt(Task task, String input) {
        recordAttempt(input);
        if(isCompleted()) { return; }
        if(task.isCorrectAnswer(input)) { markCompleted(); }
        else { markFailed("Incorrect Answer"); }
    }

    public void recordAttempt(String input) {
        this.lastInput = input;
        this.attempts++;
        if(this.status == TaskCompletionStatus.NOT_STARTED) {
            this.status = TaskCompletionStatus.IN_PROGRESS;
        }
    }

    public void markCompleted() {
        this.status = TaskCompletionStatus.COMPLETED;
        this.completedAt = LocalDate.now();
        this.lastError = null;
    }

    public void markFailed(String error) {
        this.lastError = error;
        this.status = TaskCompletionStatus.IN_PROGRESS;
    }


    // ----- Used for rebuilding this object from stored / database data ----- //

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