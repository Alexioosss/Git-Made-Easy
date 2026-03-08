package com.gitmadeeasy.usecases.taskProgress.exceptions;

public class TaskProgressNotFoundException extends RuntimeException {
    public TaskProgressNotFoundException(String userId, String taskId) {
        super(String.format("task %s progress not found for user %s", taskId, userId));
    }
}