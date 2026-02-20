package com.gitmadeeasy.entities.taskAttempts;

import java.util.List;
import java.util.Optional;

public interface TaskAttemptGateway {
    TaskProgress save(TaskProgress progress);
    Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId);
    int countCompletedTasks(String userId, String lessonId);
    List<TaskProgress> findAllByUserId(String userId);
}