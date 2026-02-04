package com.gitmadeeasy.usecases.tasks.exceptions;

public class TaskNotInLessonException extends RuntimeException {
    public TaskNotInLessonException(String taskId, String lessonId) {
        super(String.format("task %s not in lesson %s", taskId, lessonId));
    }
}