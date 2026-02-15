package com.gitmadeeasy.usecases.tasks.exceptions;

public class TaskNotFoundWithIdException extends RuntimeException {
    public TaskNotFoundWithIdException(String lessonId, String taskId) {
        super(String.format("task %s not found for lesson %s", taskId, lessonId));
    }

    public TaskNotFoundWithIdException(String taskId) {
        super(String.format("task %s not found", taskId));
    }
}