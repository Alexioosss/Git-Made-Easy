package com.gitmadeeasy.unit.infrastructure.gateways.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskDatabaseGatewayTest {
    @Mock private TaskRepository taskRepository;
    @Mock private TaskSchemaMapper taskSchemaMapper;
    @InjectMocks private TaskDatabaseGateway taskDatabaseGateway;


    @Test
    @DisplayName("Create A Task - Valid Task Is Saved And Returned")
    void createTask_WhenTaskIsValid_ReturnsSavedTask() {
        // Arrange
        Task task = provideTask();
        TaskSchema taskSchema = provideTaskSchema();
        when(this.taskSchemaMapper.toSchema(task)).thenReturn(taskSchema);
        when(this.taskRepository.save(taskSchema)).thenReturn(taskSchema);
        when(this.taskSchemaMapper.toEntity(taskSchema)).thenReturn(task);

        // Act
        Task createdTask = this.taskDatabaseGateway.createTask(task);

        // Assert
        assertNotNull(createdTask);
        assertEquals(task.getTaskId(), createdTask.getTaskId());
        verify(this.taskRepository).save(taskSchema);
        verify(this.taskSchemaMapper).toSchema(task);
        verify(this.taskSchemaMapper).toEntity(taskSchema);
    }

    @Test
    @DisplayName("Create A Task - Repository Throws An Exception / Fails")
    void createTask_WhenRepositoryFails_ThrowsException() {
        // Arrange
        Task task = provideTask();
        TaskSchema taskSchema = provideTaskSchema();
        when(this.taskSchemaMapper.toSchema(task)).thenReturn(taskSchema);
        when(this.taskRepository.save(taskSchema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.taskDatabaseGateway.createTask(task));
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson And Task Exist - Returns Task")
    void getTaskByLessonIdAndTaskId_WhenLessonAndTaskExist_ReturnsTask() {
        // Arrange
        Task task = provideTask();
        TaskSchema taskSchema = provideTaskSchema();
        String lessonId = "1";
        String taskId = "1";
        when(this.taskRepository.findByLessonIdAndTaskId(lessonId, taskId)).thenReturn(Optional.of(taskSchema));
        when(this.taskSchemaMapper.toEntity(taskSchema)).thenReturn(task);

        // Act
        Optional<Task> foundTask = this.taskDatabaseGateway.getTaskByLessonIdAndTaskId(lessonId, taskId);

        // Assert
        assertTrue(foundTask.isPresent());
        assertEquals(task, foundTask.get());
        verify(this.taskSchemaMapper).toEntity(taskSchema);
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson Exist Only - Returns Empty Optional ")
    void getTaskByLessonIdAndTaskId_WhenOnlyLessonExists_ReturnsEmptyOptional() {
        // Arrange
        String lessonId = "1";
        when(this.taskRepository.findByLessonIdAndTaskId(lessonId, null)).thenReturn(Optional.empty());

        // Act
        Optional<Task> foundTask = this.taskDatabaseGateway.getTaskByLessonIdAndTaskId(lessonId, null);

        // Assert
        assertTrue(foundTask.isEmpty());
        verify(this.taskSchemaMapper, never()).toEntity(any(TaskSchema.class));
    }

    @Test
    @DisplayName("Get Task By Lesson ID And Task ID - Lesson Does Not Exist - Returns Empty Optional")
    void getTaskByLessonIdAndTaskId_WhenLessonDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.taskRepository.findByLessonIdAndTaskId(null, null)).thenReturn(Optional.empty());

        // Act
        Optional<Task> foundTask = this.taskDatabaseGateway.getTaskByLessonIdAndTaskId(null, null);

        // Assert
        assertTrue(foundTask.isEmpty());
        verify(this.taskSchemaMapper, never()).toEntity(any(TaskSchema.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideValidTasksList")
    @DisplayName("Get Task By Lesson ID - Lesson Exists - Returns Tasks List")
    void getTasksByLessonId_WhenLessonExists_ReturnsTasksList(String displayName, List<TaskSchema> tasksList) {
        // Arrange
        String lessonId = "1";
        when(this.taskRepository.findAllByLessonId(lessonId)).thenReturn(tasksList);
        for(TaskSchema schema : tasksList) {
            Task mappedTask = new Task(
                    schema.getLessonId(), schema.getTitle(),
                    schema.getContent(), schema.getExpectedCommand(),
                    schema.getHint(), schema.getTaskOrder());
            when(this.taskSchemaMapper.toEntity(schema)).thenReturn(mappedTask);
        }

        // Act
        List<Task> foundTasks = this.taskDatabaseGateway.getTasksByLessonId(lessonId);

        // Assert
        assertEquals(tasksList.size(), foundTasks.size());
        verify(this.taskSchemaMapper, times(tasksList.size())).toEntity(any(TaskSchema.class));
    }

    @Test
    @DisplayName("Get Task By Lesson ID - Lesson Does Not Exists - Returns Empty Task List")
    void getTasksByLessonId_WhenLessonDoesNotExist_ReturnsEmptyTasksList() {
        // Arrange
        when(this.taskRepository.findAllByLessonId(null)).thenReturn(List.of());

        // Act
        List<Task> foundTasks = this.taskDatabaseGateway.getTasksByLessonId(null);

        // Assert
        assertTrue(foundTasks.isEmpty());
        verify(this.taskSchemaMapper, never()).toEntity(any(TaskSchema.class));
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Task provideTask() {
        return new Task(
                "1", "first git task", "Let's start this journey, shall we?",
                "git start", "easier than it may seem...", 1
        );
    }

    private static TaskSchema provideTaskSchema() {
        return new TaskSchema(
                "1", "first git task", "Let's start this journey, shall we?",
                "git start", "easier than it may seem...", 1
        );
    }

    private static Stream<Arguments> provideValidTasksList() {
        return Stream.of(
                Arguments.of("Empty Tasks List", List.of()),
                Arguments.of("Populated Tasks List", List.of(provideTaskSchema(), provideTaskSchema()))
        );
    }
}