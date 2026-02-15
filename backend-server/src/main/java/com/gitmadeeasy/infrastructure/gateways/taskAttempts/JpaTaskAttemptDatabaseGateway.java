package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class JpaTaskAttemptDatabaseGateway implements TaskAttemptGateway {
    private final JpaTaskAttemptRepository jpa;
    private final JpaTaskRepository jpaTaskRepository;
    private final TaskAttemptSchemaMapper taskAttemptSchemaMapper;

    public JpaTaskAttemptDatabaseGateway(JpaTaskAttemptRepository jpa, JpaTaskRepository jpaTaskRepository,
                                         TaskAttemptSchemaMapper taskAttemptSchemaMapper) {
        this.jpa = jpa;
        this.jpaTaskRepository = jpaTaskRepository;
        this.taskAttemptSchemaMapper = taskAttemptSchemaMapper;
    }

    @Override
    @Transactional
    public TaskProgress save(TaskProgress progress) {
        JpaTaskSchema task = this.jpaTaskRepository.findById(progress.getTaskId())
                .orElseThrow(() -> new TaskNotFoundWithIdException(progress.getTaskId()));

        Optional<JpaTaskAttemptSchema> existingTaskAttempt =
                this.jpa.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId());

        JpaTaskAttemptSchema schemaToSave = existingTaskAttempt
                .map(existing ->
                        this.taskAttemptSchemaMapper.updateSchemaFromEntity(existing, progress, task))
                .orElseGet(() -> this.taskAttemptSchemaMapper.toSchema(progress, task));

        return this.taskAttemptSchemaMapper.toEntity(this.jpa.saveAndFlush(schemaToSave));
    }

    @Override
    public Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId) {
        return this.jpa.findByUserIdAndTaskId(userId, taskId).map(this.taskAttemptSchemaMapper::toEntity);
    }

    @Override
    public int countCompletedTasks(String userId, String lessonId) {
        return this.jpa.countCompletedTasks(userId, lessonId);
    }
}