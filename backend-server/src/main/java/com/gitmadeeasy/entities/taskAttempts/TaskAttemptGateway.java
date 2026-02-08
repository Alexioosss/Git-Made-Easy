package com.gitmadeeasy.entities.taskAttempts;

import java.util.Optional;

public interface TaskAttemptGateway {
    TaskProgress save(TaskProgress progress);
    Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId);
    int countCompletedTasks(String userId, String lessonId);
}