package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;

import java.util.Optional;

public class JpaTaskAttemptDatabaseGateway implements TaskAttemptGateway {
    private final JpaTaskAttemptRepository jpa;
    private final TaskAttemptSchemaMapper taskAttemptSchemaMapper;

    public JpaTaskAttemptDatabaseGateway(JpaTaskAttemptRepository jpa, TaskAttemptSchemaMapper taskAttemptSchemaMapper) {
        this.jpa = jpa;
        this.taskAttemptSchemaMapper = taskAttemptSchemaMapper;
    }

    @Override
    public TaskProgress save(TaskProgress progress) {
        Optional<JpaTaskAttemptSchema> existingTaskAttempt =
                this.jpa.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId());

        JpaTaskAttemptSchema schemaToSave = existingTaskAttempt.map(existing ->
                        this.taskAttemptSchemaMapper.merge(existing, progress))
                .orElseGet(() -> this.taskAttemptSchemaMapper.toSchema(progress));

        return this.taskAttemptSchemaMapper.toEntity(this.jpa.save(schemaToSave));
    }

    @Override
    public Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId) {
        return this.jpa.findByUserIdAndTaskId(userId, taskId)
                .map(this.taskAttemptSchemaMapper::toEntity);
    }

    @Override
    public int countCompletedTasks(String userId, String lessonId) {
        return this.jpa.countCompletedTasks(userId, lessonId);
    }
}