package com.gitmadeeasy.unit.infrastructure.gateways.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import com.gitmadeeasy.testUtil.TaskTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JpaTaskDatabaseGatewayTest {
    @Mock private JpaTaskRepository taskRepository;
    @Mock private TaskSchemaMapper taskSchemaMapper;
    @InjectMocks private JpaTaskDatabaseGateway jpaTaskDatabaseGateway;


    @Test
    @DisplayName("Create A Task - Valid Task Is Saved And Returned")
    void createTask_WhenTaskIsValid_ReturnsSavedTask() {
        // Arrange
        Task task = TaskTestDataFactory.task();
        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();
        when(this.taskSchemaMapper.toJpaSchema(task)).thenReturn(taskSchema);
        when(this.taskRepository.save(taskSchema)).thenReturn(taskSchema);
        when(this.taskSchemaMapper.fromJpaSchema(taskSchema)).thenReturn(task);

        // Act
        Task createdTask = this.jpaTaskDatabaseGateway.createTask(task);

        // Assert
        assertNotNull(createdTask);
        assertEquals(task.getTaskId(), createdTask.getTaskId());
        verify(this.taskRepository).save(taskSchema);
        verify(this.taskSchemaMapper).toJpaSchema(task);
        verify(this.taskSchemaMapper).fromJpaSchema(taskSchema);
    }

    @Test
    @DisplayName("Create A Task - Repository Throws An Exception / Fails")
    void createTask_WhenRepositoryFails_ThrowsException() {
        // Arrange
        Task task = TaskTestDataFactory.task();
        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();
        when(this.taskSchemaMapper.toJpaSchema(task)).thenReturn(taskSchema);
        when(this.taskRepository.save(taskSchema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.jpaTaskDatabaseGateway.createTask(task));
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson And Task Exist - Returns Task")
    void getTaskByLessonIdAndTaskId_WhenLessonAndTaskExist_ReturnsTask() {
        // Arrange
        Task task = TaskTestDataFactory.task();
        JpaTaskSchema taskSchema = TaskTestDataFactory.jpaTaskSchema();
        String lessonId = "1";
        String taskId = "1";
        when(this.taskRepository.findByLessonIdAndId(lessonId, taskId)).thenReturn(Optional.of(taskSchema));
        when(this.taskSchemaMapper.fromJpaSchema(taskSchema)).thenReturn(task);

        // Act
        Optional<Task> foundTask = this.jpaTaskDatabaseGateway.getTaskByLessonIdAndTaskId(lessonId, taskId);

        // Assert
        assertTrue(foundTask.isPresent());
        assertEquals(task, foundTask.get());
        verify(this.taskSchemaMapper).fromJpaSchema(taskSchema);
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson Exist Only - Returns Empty Optional ")
    void getTaskByLessonIdAndTaskId_WhenOnlyLessonExists_ReturnsEmptyOptional() {
        // Arrange
        String lessonId = "1";
        when(this.taskRepository.findByLessonIdAndId(lessonId, null)).thenReturn(Optional.empty());

        // Act
        Optional<Task> foundTask = this.jpaTaskDatabaseGateway.getTaskByLessonIdAndTaskId(lessonId, null);

        // Assert
        assertTrue(foundTask.isEmpty());
        verify(this.taskSchemaMapper, never()).fromJpaSchema(any(JpaTaskSchema.class));
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson Does Not Exist - Returns Empty Optional")
    void getTaskByLessonIdAndTaskId_WhenLessonDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.taskRepository.findByLessonIdAndId(null, null)).thenReturn(Optional.empty());

        // Act
        Optional<Task> foundTask = this.jpaTaskDatabaseGateway.getTaskByLessonIdAndTaskId(null, null);

        // Assert
        assertTrue(foundTask.isEmpty());
        verify(this.taskSchemaMapper, never()).fromJpaSchema(any(JpaTaskSchema.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("com.gitmadeeasy.testUtil.TaskTestDataFactory#validTasksList")
    @DisplayName("Get Task By Lesson ID - Lesson Exists - Returns Tasks List")
    void getTasksByLessonId_WhenLessonExists_ReturnsTasksList(String displayName, List<JpaTaskSchema> tasksList) {
        // Arrange
        String lessonId = "1";
        when(this.taskRepository.findAllByLessonId(lessonId)).thenReturn(tasksList);
        tasksList.forEach(schema ->
                when(this.taskSchemaMapper.fromJpaSchema(schema)).thenReturn(TaskTestDataFactory.map(schema)));

        // Act
        List<Task> foundTasks = this.jpaTaskDatabaseGateway.getTasksByLessonId(lessonId);

        // Assert
        assertEquals(tasksList.size(), foundTasks.size());
        verify(this.taskSchemaMapper, times(tasksList.size())).fromJpaSchema(any(JpaTaskSchema.class));
    }

    @Test
    @DisplayName("Get Task By Lesson ID - Lesson Does Not Exists - Returns Empty Task List")
    void getTasksByLessonId_WhenLessonDoesNotExist_ReturnsEmptyTasksList() {
        // Arrange
        when(this.taskRepository.findAllByLessonId(null)).thenReturn(List.of());

        // Act
        List<Task> foundTasks = this.jpaTaskDatabaseGateway.getTasksByLessonId(null);

        // Assert
        assertTrue(foundTasks.isEmpty());
        verify(this.taskSchemaMapper, never()).fromJpaSchema(any(JpaTaskSchema.class));
    }
}