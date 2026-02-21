package com.gitmadeeasy.infrastructure.mappers.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.FirebaseTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;

import java.time.LocalDate;

public class TaskAttemptSchemaMapper {

    public JpaTaskAttemptSchema toSchema(TaskProgress entity, JpaTaskSchema task) {
        return new JpaTaskAttemptSchema(
                entity.getUserId(), entity.getStatus(),
                entity.getAttempts(), entity.getLastInput(), entity.getLastError(),
                entity.getStartedAt(), entity.getCompletedAt(), task);
    }

    public JpaTaskAttemptSchema updateSchemaFromEntity(JpaTaskAttemptSchema existing,
                                                       TaskProgress entity, JpaTaskSchema task) {
        existing.setTask(task);
        existing.setStatus(entity.getStatus());
        existing.setAttempts(entity.getAttempts());
        existing.setLastInput(entity.getLastInput());
        existing.setLastError(entity.getLastError());
        existing.setStartedAt(entity.getStartedAt());
        existing.setCompletedAt(entity.getCompletedAt());
        return existing;
    }



    // ----- Firebase-Related Mapping ----- //

    public TaskProgress toEntity(JpaTaskAttemptSchema schema) {
        return new TaskProgress(
                schema.getId(), schema.getUserId(), schema.getTask().getId(),
                schema.getTask().getLessonId(), schema.getTask().getTitle(),
                schema.getStatus(), schema.getAttempts(), schema.getLastInput(),
                schema.getLastError(), schema.getStartedAt(), schema.getCompletedAt());
    }

    public FirebaseTaskAttemptSchema toFirebaseSchema(TaskProgress entity) {
        String startedAt = entity.getStartedAt() != null ? entity.getStartedAt().toString() : null;
        String completedAt = entity.getCompletedAt() != null ? entity.getCompletedAt().toString() : null;

        FirebaseTaskAttemptSchema schema = new FirebaseTaskAttemptSchema(
                entity.getUserId(), entity.getTaskId(),
                entity.getStatus(), entity.getAttempts(),
                entity.getLastInput(), entity.getLastError(),
                startedAt, completedAt);

        if(entity.getTaskProgressId() != null) { schema.setId(entity.getTaskProgressId()); }
        return schema;
    }

    public TaskProgress fromFirebaseSchema(FirebaseTaskAttemptSchema schema) {
        LocalDate startedAt = schema.getStartedAt() != null ? LocalDate.parse(schema.getStartedAt()) : null;
        LocalDate completedAt = schema.getCompletedAt() != null ? LocalDate.parse(schema.getCompletedAt()) : null;
        return new TaskProgress(
                schema.getId(), schema.getUserId(), schema.getTaskId(),
                schema.getLessonId(), schema.getTaskTitle(),
                schema.getStatus(), schema.getAttempts(), schema.getLastInput(),
                schema.getLastError(), startedAt, completedAt);
    }
}