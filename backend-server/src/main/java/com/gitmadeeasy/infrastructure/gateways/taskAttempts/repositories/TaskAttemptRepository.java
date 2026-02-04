package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;

import java.util.Optional;

public interface TaskAttemptRepository {
    TaskAttemptSchema save(TaskAttemptSchema taskAttemptSchema);
    Optional<TaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId);
}