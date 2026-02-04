package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;

import java.util.Optional;

public class ConcreteJpaTaskAttemptRepository implements TaskAttemptRepository {
    private final AbstractJpaTaskAttemptRepository jpa;

    public ConcreteJpaTaskAttemptRepository(AbstractJpaTaskAttemptRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public TaskAttemptSchema save(TaskAttemptSchema taskAttemptSchema) {
        return this.jpa.save(taskAttemptSchema);
    }

    @Override
    public Optional<TaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId) {
        return this.jpa.findByUserIdAndTaskId(userId, taskId);
    }
}