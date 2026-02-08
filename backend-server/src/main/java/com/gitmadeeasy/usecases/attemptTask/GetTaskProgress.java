package com.gitmadeeasy.usecases.attemptTask;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;

import java.util.Optional;

public class GetTaskProgress {
    private final TaskAttemptGateway taskAttemptGateway;
    private final LessonGateway lessonGateway;

    public GetTaskProgress(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.lessonGateway = lessonGateway;
    }

    public Optional<TaskProgress> execute(String userId, String lessonId, String taskId) {
        if(!this.lessonGateway.existsById(lessonId)) { throw new LessonNotFoundWithIdException(lessonId); }
        return this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId);
    }
}