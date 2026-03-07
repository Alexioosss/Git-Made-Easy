package com.gitmadeeasy.unit.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;
import com.gitmadeeasy.testUtil.TaskTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttemptTaskDatabaseGatewayTest {
    @Mock private JpaTaskAttemptRepository taskAttemptRepository;
    @Mock private JpaTaskRepository taskRepository;
    @Mock private TaskAttemptSchemaMapper taskAttemptSchemaMapper;
    @InjectMocks private JpaTaskAttemptDatabaseGateway jpaTaskAttemptDatabaseGateway;


    @Test
    @DisplayName("Save Task Attempt - New Task Progress - Returns TaskProgress")
    void save_WhenNewTaskProgress_ReturnsSavedTaskProgress() {
        // Arrange
        TaskProgress progress = TaskTestDataFactory.taskProgress();
        JpaTaskAttemptSchema schema = TaskTestDataFactory.taskAttemptSchema();
        JpaTaskAttemptSchema savedSchema = TaskTestDataFactory.taskAttemptSchemaWithId("1");
        TaskProgress savedProgress = TaskTestDataFactory.taskProgressWithId("1");

        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();

        when(this.taskRepository.findById(progress.getTaskId())).thenReturn(Optional.of(taskSchema));
        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.empty());
        when(this.taskAttemptSchemaMapper.toSchema(eq(progress), any(JpaTaskSchema.class))).thenReturn(schema);
        when(this.taskAttemptRepository.saveAndFlush(schema)).thenReturn(savedSchema);
        when(this.taskAttemptSchemaMapper.toEntity(any(JpaTaskAttemptSchema.class))).thenReturn(savedProgress);

        // Act
        TaskProgress result = this.jpaTaskAttemptDatabaseGateway.save(progress);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getTaskProgressId());
        verify(this.taskAttemptRepository).saveAndFlush(schema);
        verify(this.taskAttemptSchemaMapper).toSchema(eq(progress), any(JpaTaskSchema.class));
        verify(this.taskAttemptSchemaMapper).toEntity(any(JpaTaskAttemptSchema.class));
    }

    @Test
    @DisplayName("Save Task Attempt - Task Progress Already Exists - Updates Saved Task Progress")
    void save_WhenExistingTaskProgress_ReturnsUpdatedTaskProgress() {
        // Arrange
        TaskProgress progress = TaskTestDataFactory.taskProgress();
        JpaTaskAttemptSchema existingSchema = TaskTestDataFactory.taskAttemptSchemaWithId("1");
        JpaTaskAttemptSchema schemaToSave = TaskTestDataFactory.taskAttemptSchema();
        JpaTaskAttemptSchema savedSchema = TaskTestDataFactory.taskAttemptSchemaWithId("1");
        TaskProgress updatedProgress = TaskTestDataFactory.taskProgressWithId("1");
        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();

        when(this.taskRepository.findById(progress.getTaskId())).thenReturn(Optional.of(taskSchema));
        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.of(existingSchema));
        when(this.taskAttemptSchemaMapper.updateSchemaFromEntity(eq(existingSchema), eq(progress), eq(taskSchema)))
                .thenReturn(schemaToSave);
        when(this.taskAttemptRepository.saveAndFlush(schemaToSave)).thenReturn(savedSchema);
        when(this.taskAttemptSchemaMapper.toEntity(any(JpaTaskAttemptSchema.class))).thenReturn(updatedProgress);

        // Act
        TaskProgress result = this.jpaTaskAttemptDatabaseGateway.save(progress);


        // Assert
        assertNotNull(result);
        assertEquals("1", result.getTaskProgressId());
        verify(this.taskAttemptRepository).saveAndFlush(schemaToSave);
        verify(this.taskAttemptSchemaMapper).updateSchemaFromEntity(eq(existingSchema), eq(progress), eq(taskSchema));
        verify(this.taskAttemptSchemaMapper).toEntity(any(JpaTaskAttemptSchema.class));
    }

    @Test
    @DisplayName("Save Task Attempt - Repository Fails")
    void save_WhenRepositoryFails_ThrowsException() {
        // Arrange
        TaskProgress progress = TaskTestDataFactory.taskProgress();
        JpaTaskAttemptSchema schema = TaskTestDataFactory.taskAttemptSchema();
        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();
        when(this.taskRepository.findById(progress.getTaskId())).thenReturn(Optional.of(taskSchema));
        when(this.taskAttemptRepository.findByUserIdAndTaskId(progress.getUserId(), progress.getTaskId()))
                .thenReturn(Optional.empty());
        when(this.taskAttemptSchemaMapper.toSchema(eq(progress), any(JpaTaskSchema.class))).thenReturn(schema);
        when(this.taskAttemptRepository.saveAndFlush(schema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.jpaTaskAttemptDatabaseGateway.save(progress));
        verify(this.taskAttemptRepository).saveAndFlush(schema);
    }

    @Test
    @DisplayName("Find Task Attempt - Record Exists")
    void findByUserIdAndTaskId_WhenTaskAttemptExists_ReturnsTaskProgress() {
        // Arrange
        JpaTaskAttemptSchema schema = TaskTestDataFactory.taskAttemptSchemaWithId("1");
        TaskProgress mapped = TaskTestDataFactory.taskProgressWithId("1");
        when(this.taskAttemptRepository.findByUserIdAndTaskId("1", "1")).thenReturn(Optional.of(schema));
        when(this.taskAttemptSchemaMapper.toEntity(schema)).thenReturn(mapped);

        // Act
        Optional<TaskProgress> result = this.jpaTaskAttemptDatabaseGateway.findByUserIdAndTaskId("1", "1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getTaskProgressId());
        verify(this.taskAttemptSchemaMapper).toEntity(schema);
    }

    @Test
    @DisplayName("Find Task Attempt - Record Does Not Exists")
    void findByUserIdAndTaskId_WhenTaskAttemptDoesNotExist_ReturnsNull() {
        // Arrange
        when(this.taskAttemptRepository.findByUserIdAndTaskId("1", "1")).thenReturn(Optional.empty());

        // Act
        Optional<TaskProgress> result = this.jpaTaskAttemptDatabaseGateway.findByUserIdAndTaskId("1", "1");

        // Assert
        assertTrue(result.isEmpty());
        verify(this.taskAttemptSchemaMapper, never()).toEntity(any());
    }
}