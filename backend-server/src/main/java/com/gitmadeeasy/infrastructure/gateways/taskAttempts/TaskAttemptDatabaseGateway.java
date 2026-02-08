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
        TaskAttemptSchema existingSchema = this.taskAttemptRepository
                .findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()).orElse(null);
        TaskAttemptSchema schema = this.taskAttemptSchemaMapper.toSchema(progress);

        if(existingSchema != null) { schema.setTaskProgressId(Long.valueOf(existingSchema.getTaskProgressId())); }

        return this.taskAttemptSchemaMapper.toEntity(this.taskAttemptRepository.save(schema));
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