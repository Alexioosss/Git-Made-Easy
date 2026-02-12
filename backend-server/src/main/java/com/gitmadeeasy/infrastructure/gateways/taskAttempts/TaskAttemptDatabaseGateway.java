package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;

import java.util.Optional;

public class TaskAttemptDatabaseGateway implements TaskAttemptGateway {
    private final TaskAttemptRepository taskAttemptRepository;
    private final TaskAttemptSchemaMapper taskAttemptSchemaMapper;

    public TaskAttemptDatabaseGateway(TaskAttemptRepository taskAttemptRepository, TaskAttemptSchemaMapper taskAttemptSchemaMapper) {
        this.taskAttemptRepository = taskAttemptRepository;
        this.taskAttemptSchemaMapper = taskAttemptSchemaMapper;
    }

    @Override
    public TaskProgress save(TaskProgress progress) {
        Optional<TaskAttemptSchema> existingTaskAttempt =
                this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId());

        TaskAttemptSchema schemaToSave = existingTaskAttempt.map(existing ->
                        this.taskAttemptSchemaMapper.merge(existing, progress))
                .orElseGet(() -> this.taskAttemptSchemaMapper.toSchema(progress));

        return this.taskAttemptSchemaMapper.toEntity(this.taskAttemptRepository.save(schemaToSave));
    }

    @Override
    public Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId) {
        return this.taskAttemptRepository.findByUserIdAndTaskId(userId, taskId)
                .map(this.taskAttemptSchemaMapper::toEntity);
    }

    @Override
    public int countCompletedTasks(String userId, String lessonId) {
        return this.taskAttemptRepository.countCompletedTasks(userId, lessonId);
    }
}