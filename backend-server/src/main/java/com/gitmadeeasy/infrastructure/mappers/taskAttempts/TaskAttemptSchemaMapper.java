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

    public TaskProgress toEntity(TaskAttemptSchema schema) {
        TaskProgress domain = new TaskProgress(
                schema.getTaskProgressId(),
                schema.getUserId(),
                schema.getTaskId()
        );

        domain.setStatus(schema.getStatus());
        domain.setAttempts(schema.getAttempts());
        domain.setLastInput(schema.getLastInput());
        domain.setLastError(schema.getLastError());
        domain.setStartedAt(schema.getStartedAt());
        domain.setCompletedAt(schema.getCompletedAt());

        return domain;
    }
}