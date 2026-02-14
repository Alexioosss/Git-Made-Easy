package com.gitmadeeasy.infrastructure.mappers.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.FirebaseTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;

public class TaskAttemptSchemaMapper {

    public JpaTaskAttemptSchema toSchema(TaskProgress entity) {
        return new JpaTaskAttemptSchema(
                entity.getUserId(), entity.getTaskId(), entity.getStatus(),
                entity.getAttempts(), entity.getLastInput(), entity.getLastError(),
                entity.getStartedAt(), entity.getCompletedAt());
    }

    public JpaTaskAttemptSchema merge(JpaTaskAttemptSchema existing, TaskProgress entity) {
        existing.setStatus(entity.getStatus());
        existing.setAttempts(entity.getAttempts());
        existing.setLastInput(entity.getLastInput());
        existing.setLastError(entity.getLastError());
        existing.setStartedAt(entity.getStartedAt());
        existing.setCompletedAt(entity.getCompletedAt());

        return existing;
    }

    public TaskProgress toEntity(JpaTaskAttemptSchema schema) {
        return new TaskProgress(
                schema.getId(), schema.getUserId(), schema.getTaskId(),
                schema.getStatus(), schema.getAttempts(), schema.getLastInput(),
                schema.getLastError(), schema.getStartedAt(), schema.getCompletedAt());
    }

    public FirebaseTaskAttemptSchema toFirebaseSchema(TaskProgress entity) {
        FirebaseTaskAttemptSchema schema = new FirebaseTaskAttemptSchema(
                entity.getUserId(), entity.getTaskId(),
                entity.getStatus(), entity.getAttempts(),
                entity.getLastInput(), entity.getLastError(),
                entity.getStartedAt(), entity.getCompletedAt());

        if(entity.getTaskProgressId() != null) { schema.setId(entity.getTaskProgressId()); }
        return schema;
    }

    public TaskProgress fromFirebaseSchema(FirebaseTaskAttemptSchema schema) {
        return new TaskProgress(
                schema.getId(), schema.getUserId(), schema.getTaskId(),
                schema.getStatus(), schema.getAttempts(), schema.getLastInput(),
                schema.getLastError(), schema.getStartedAt(), schema.getCompletedAt());
    }
}