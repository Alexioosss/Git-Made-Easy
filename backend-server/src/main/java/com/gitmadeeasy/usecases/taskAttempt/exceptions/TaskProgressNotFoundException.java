package com.gitmadeeasy.usecases.taskAttempt.exceptions;

public class TaskProgressNotFoundException extends RuntimeException {
    public TaskProgressNotFoundException(String userId, String taskId) {
        super(String.format("user %s's task %s's progress not found", userId, taskId));
    }
}