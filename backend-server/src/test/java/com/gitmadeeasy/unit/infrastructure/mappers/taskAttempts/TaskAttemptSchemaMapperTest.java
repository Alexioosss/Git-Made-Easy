package com.gitmadeeasy.unit.infrastructure.mappers.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskAttemptSchemaMapperTest {
    private static final TaskAttemptSchemaMapper mapper = new TaskAttemptSchemaMapper();

    @Test
    @DisplayName("To Schema - Maps Task Progress Domain Entity To Persistence Schema")
    void toSchema_WhenGivenDomainTaskProgress_MapsToPersistenceSchema() {
        // Arrange
        LocalDate startedAt = LocalDate.now();
        LocalDate completedAt = LocalDate.now().plusDays(1);
        TaskProgress progress = new TaskProgress( "1", "1", "1", TaskCompletionStatus.IN_PROGRESS,
                1, "git statuss", "Incorrect answer", startedAt, completedAt);

        // Act
        TaskAttemptSchema schema = mapper.toSchema(progress);

        // Assert
        assertEquals("1", schema.getUserId());
        assertEquals("1", schema.getTaskId());
        assertEquals(TaskCompletionStatus.IN_PROGRESS, schema.getStatus());
        assertEquals(1, schema.getAttempts());
        assertEquals("git statuss", schema.getLastInput());
        assertEquals("Incorrect answer", schema.getLastError());
        assertEquals(startedAt, schema.getStartedAt());
        assertEquals(completedAt, schema.getCompletedAt());
    }

    @Test
    @DisplayName("Update Schema - Updates Existing Persistence Schema")
    void merge_WhenGivenExistingPersistenceSchema_ReturnsUpdatedPersistenceSchema() {
        // Arrange
        LocalDate startedAt = LocalDate.now().minusDays(2);
        LocalDate completedAt = LocalDate.now().minusDays(1);
        TaskAttemptSchema existingSchema = new TaskAttemptSchema(
                "1", "1", TaskCompletionStatus.IN_PROGRESS, 2,
                "git int", "Incorrect answer", startedAt, completedAt);
        existingSchema.setTaskProgressId(1L);
        LocalDate newStartedAt = LocalDate.now();
        LocalDate newCompletedAt = LocalDate.now();
        TaskProgress updatedTaskProgress = new TaskProgress(
                "ignored", "1", "1", TaskCompletionStatus.COMPLETED, 3,
                "git init", "", newStartedAt, newCompletedAt);

        // Act
        TaskAttemptSchema updatedSchema = mapper.merge(existingSchema, updatedTaskProgress);

        // Assert
        assertEquals("1", updatedSchema.getTaskProgressId());
        assertEquals(TaskCompletionStatus.COMPLETED, updatedSchema.getStatus());
        assertEquals(3, updatedSchema.getAttempts());
        assertEquals("git init", updatedSchema.getLastInput());
        assertEquals("", updatedSchema.getLastError());
        assertEquals(newStartedAt, updatedSchema.getStartedAt());
        assertEquals(newCompletedAt, updatedSchema.getCompletedAt());
    }

    @Test
    @DisplayName("To Entity - Maps Persistence Schema To Task Progress Domain Entity")
    void toEntity_WhenGivenPersistenceSchema_MapsToDomainTaskProgress() {
        // Arrange
        LocalDate startedAt = LocalDate.now();
        LocalDate completedAt = LocalDate.now().plusDays(1);
        TaskAttemptSchema schema = new TaskAttemptSchema( "1", "1", TaskCompletionStatus.COMPLETED,
                1, "git start", null, startedAt, completedAt);
        schema.setTaskProgressId(Long.valueOf("1"));

        // Act
        TaskProgress progress = mapper.toEntity(schema);

        // Assert
        assertEquals("1", progress.getTaskProgressId());
        assertEquals("1", progress.getUserId());
        assertEquals("1", progress.getTaskId());
        assertEquals(TaskCompletionStatus.COMPLETED, progress.getStatus());
        assertEquals(1, progress.getAttempts());
        assertEquals("git start", progress.getLastInput());
        assertNull(progress.getLastError());
        assertEquals(startedAt, progress.getStartedAt());
        assertEquals(completedAt, progress.getCompletedAt());
    }
}