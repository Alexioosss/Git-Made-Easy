package com.gitmadeeasy.unit.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttemptTaskDatabaseGatewayTest {
    @Mock private TaskAttemptRepository taskAttemptRepository;
    @Mock private TaskAttemptSchemaMapper taskAttemptSchemaMapper;
    @InjectMocks private TaskAttemptDatabaseGateway taskAttemptDatabaseGateway;


    @Test
    @DisplayName("Save Task Attempt - New Task Progress - Returns TaskProgress")
    void save_WhenNewTaskProgress_ReturnsSavedTaskProgress() {
        // Arrange
        TaskProgress progress = provideTaskProgress();
        TaskAttemptSchema schema = provideTaskAttemptSchema();
        TaskAttemptSchema savedSchema = provideTaskAttemptSchemaWithId("1");
        TaskProgress savedProgress = provideTaskProgressWithId("1");

        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.empty());
        when(this.taskAttemptSchemaMapper.toSchema(progress)).thenReturn(schema);
        when(this.taskAttemptRepository.save(schema)).thenReturn(savedSchema);
        when(this.taskAttemptSchemaMapper.toEntity(savedSchema)).thenReturn(savedProgress);

        // Act
        TaskProgress result = this.taskAttemptDatabaseGateway.save(progress);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getTaskProgressId());
        verify(this.taskAttemptRepository).save(schema);
        verify(this.taskAttemptSchemaMapper).toSchema(progress);
        verify(this.taskAttemptSchemaMapper).toEntity(savedSchema);
    }

    @Test
    @DisplayName("Save Task Attempt - Task Progress Already Exists - Updates Saved Task Progress")
    void save_WhenExistingTaskProgress_ReturnsUpdatedTaskProgress() {
        // Arrange
        TaskProgress progress = provideTaskProgress();
        TaskAttemptSchema existingSchema = provideTaskAttemptSchemaWithId("1");
        TaskAttemptSchema schemaToSave = provideTaskAttemptSchema();
        TaskAttemptSchema savedSchema = provideTaskAttemptSchemaWithId("1");
        TaskProgress updatedProgress = provideTaskProgressWithId("1");

        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.of(existingSchema));
        when(this.taskAttemptSchemaMapper.toSchema(progress)).thenReturn(schemaToSave);
        when(this.taskAttemptRepository.save(schemaToSave)).thenReturn(savedSchema);
        when(this.taskAttemptSchemaMapper.toEntity(savedSchema)).thenReturn(updatedProgress);

        // Act
        TaskProgress result = this.taskAttemptDatabaseGateway.save(progress);


        // Assert
        assertNotNull(result);
        assertEquals("1", result.getTaskProgressId());
        verify(this.taskAttemptRepository).save(schemaToSave);
        verify(this.taskAttemptSchemaMapper).toSchema(progress);
        verify(this.taskAttemptSchemaMapper).toEntity(savedSchema);
    }

    @Test
    @DisplayName("Save Task Attempt - Repository Throws An Exception / Fails")
    void save_WhenRepositoryFails_ThrowsException() {
        // Arrange
        TaskProgress progress = provideTaskProgress();
        TaskAttemptSchema schema = provideTaskAttemptSchema();
        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.empty());
        when(this.taskAttemptSchemaMapper.toSchema(progress)).thenReturn(schema);
        when(this.taskAttemptRepository.save(schema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.taskAttemptDatabaseGateway.save(progress));
    }

    @Test
    @DisplayName("Find Task Attempt - Record Exists")
    void findByUserIdAndTaskId_WhenTaskAttemptExists_ReturnsTaskProgress() {
        // Arrange
        TaskAttemptSchema schema = provideTaskAttemptSchemaWithId("1");
        TaskProgress mapped = provideTaskProgressWithId("1");
        when(taskAttemptRepository.findByUserIdAndTaskId("1", "1")).thenReturn(Optional.of(schema));
        when(taskAttemptSchemaMapper.toEntity(schema)).thenReturn(mapped);

        // Act
        Optional<TaskProgress> result = this.taskAttemptDatabaseGateway.findByUserIdAndTaskId("1", "1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getTaskProgressId());
        verify(this.taskAttemptSchemaMapper).toEntity(schema);
    }

    @Test
    @DisplayName("Find Task Attempt - Record Does Not Exists")
    void findByUserIdAndTaskId_WhenTaskAttemptDoesNotExist_ReturnsNull() {
        // Arrange
        when(taskAttemptRepository.findByUserIdAndTaskId("1", "1")).thenReturn(Optional.empty());

        // Act
        Optional<TaskProgress> result = this.taskAttemptDatabaseGateway.findByUserIdAndTaskId("1", "1");

        // Assert
        assertTrue(result.isEmpty());
        verify(this.taskAttemptSchemaMapper, never()).toEntity(any());
    }



    // ----- HELPER METHOD SOURCES ----- //


    private TaskProgress provideTaskProgress() {
        TaskProgress progress = new TaskProgress("1", "1", "1");

        progress.setStatus(TaskCompletionStatus.IN_PROGRESS);
        progress.setAttempts(1);
        progress.setLastInput("input");
        progress.setLastError(null);
        progress.setStartedAt(LocalDate.now());
        progress.setCompletedAt(null);

        return progress;
    }

    private TaskProgress provideTaskProgressWithId(String id) {
        TaskProgress progress = provideTaskProgress();
        return new TaskProgress(id, progress.getUserId(), progress.getTaskId());
    }

    private TaskAttemptSchema provideTaskAttemptSchema() {
        return new TaskAttemptSchema(
                "1", "1", TaskCompletionStatus.IN_PROGRESS, 1,
                "input", null, LocalDate.now(), null
        );
    }

    private TaskAttemptSchema provideTaskAttemptSchemaWithId(String id) {
        TaskAttemptSchema schema = provideTaskAttemptSchema();
        schema.setTaskProgressId(Long.valueOf(id));
        return schema;
    }
}