package com.gitmadeeasy.usecases.taskProgress;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetTaskProgress {
    private final TaskAttemptGateway taskAttemptGateway;
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(GetTaskProgress.class);

    public GetTaskProgress(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.lessonGateway = lessonGateway;
    }

    public Optional<TaskProgress> execute(String userId, String lessonId, String taskId) {
        if(!this.lessonGateway.existsById(lessonId)) {
            log.warn("Lesson not found with Lesson ID {}", lessonId);
            throw new LessonNotFoundWithIdException(lessonId);
        }
        log.info("Lesson found successfully");
        return this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId);
    }
}