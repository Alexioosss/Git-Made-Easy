package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.usecases.taskAttempt.exceptions.TaskProgressNotFoundException;

public class TaskAttemptDatabaseGateway implements TaskAttemptGateway {
    private final TaskAttemptRepository taskAttemptRepository;
    private final TaskAttemptSchemaMapper taskAttemptSchemaMapper;

    public TaskAttemptDatabaseGateway(TaskAttemptRepository taskAttemptRepository, TaskAttemptSchemaMapper taskAttemptSchemaMapper) {
        this.taskAttemptRepository = taskAttemptRepository;
        this.taskAttemptSchemaMapper = taskAttemptSchemaMapper;
    }

    @Override
    public TaskProgress save(TaskProgress progress) {
        TaskAttemptSchema savedTaskAttemptSchema = this.taskAttemptRepository.save(
                this.taskAttemptSchemaMapper.toSchema(progress)
        );
        return this.taskAttemptSchemaMapper.toEntity(savedTaskAttemptSchema);
    }

    @Override
    public TaskProgress findByUserIdAndTaskId(String userId, String taskId) {
        return this.taskAttemptRepository.findByUserIdAndTaskId(userId, taskId)
                .map(this.taskAttemptSchemaMapper::toEntity)
                .orElse(null);
    }
}