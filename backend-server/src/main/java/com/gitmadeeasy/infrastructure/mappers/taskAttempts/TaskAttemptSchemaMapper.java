package com.gitmadeeasy.infrastructure.mappers.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;

public class TaskAttemptSchemaMapper {

    public TaskAttemptSchema toSchema(TaskProgress entity) {
        return new TaskAttemptSchema(
                entity.getUserId(),
                entity.getTaskId(),
                entity.getStatus(),
                entity.getAttempts(),
                entity.getLastInput(),
                entity.getLastError(),
                entity.getStartedAt(),
                entity.getCompletedAt()
        );
    }

    public TaskAttemptSchema merge(TaskAttemptSchema existing, TaskProgress entity) {
        existing.setStatus(entity.getStatus());
        existing.setAttempts(entity.getAttempts());
        existing.setLastInput(entity.getLastInput());
        existing.setLastError(entity.getLastError());
        existing.setStartedAt(entity.getStartedAt());
        existing.setCompletedAt(entity.getCompletedAt());
        return existing;
    }

    public TaskProgress toEntity(TaskAttemptSchema schema) {
        return new TaskProgress(
                schema.getTaskProgressId(),
                schema.getUserId(),
                schema.getTaskId(),
                schema.getStatus(),
                schema.getAttempts(),
                schema.getLastInput(),
                schema.getLastError(),
                schema.getStartedAt(),
                schema.getCompletedAt()
        );
    }
}