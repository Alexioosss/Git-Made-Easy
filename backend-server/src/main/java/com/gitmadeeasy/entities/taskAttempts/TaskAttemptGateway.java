package com.gitmadeeasy.entities.taskAttempts;

public interface TaskAttemptGateway {
    TaskProgress save(TaskProgress progress);
    TaskProgress findByUserIdAndTaskId(String userId, String taskId);
}