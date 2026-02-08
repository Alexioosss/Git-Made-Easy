package com.gitmadeeasy.usecases.taskAttempt;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTaskProgressTest {
    private TaskAttemptGateway taskAttemptGateway;
    private LessonGateway lessonGateway;
    private GetTaskProgress getTaskProgress;

    @BeforeEach
    void setUp() {
        taskAttemptGateway = mock(TaskAttemptGateway.class);
        lessonGateway = mock(LessonGateway.class);
        getTaskProgress = new GetTaskProgress(taskAttemptGateway, lessonGateway);
    }

    @Test
    @DisplayName("Get Task Progress - Lesson, Task And User Task Progress Exist - Returns The User's Task Progress")
    void execute_WhenLessonAndTaskAndUserTaskProgressExist_ReturnsTaskProgress() {
        // Arrange
        String lessonId = "1", taskId = "1", userId = "1";
        TaskProgress taskProgress = provideTaskProgress(userId, taskId);
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId))
                .thenReturn(Optional.of(taskProgress));

        // Act
        Optional<TaskProgress> userTaskProgress = this.getTaskProgress.execute(userId, lessonId, taskId);

        //Assert
        assertTrue(userTaskProgress.isPresent());
        assertEquals(taskProgress, userTaskProgress.get());
        verify(this.taskAttemptGateway).findByUserIdAndTaskId(userId, taskId);
    }

    @Test
    @DisplayName("Get Task Progress - User Task Progress Does Not Exist - Returns Null")
    void execute_WhenUserTaskProgressDoesNotExist_ReturnsNull() {
        // Arrange
        String lessonId = "1", taskId = "1", userId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId))
                .thenReturn(Optional.empty()); // Mocks the data store's return of a not-found entity

        // Act
        Optional<TaskProgress> userTaskProgress = this.getTaskProgress.execute(userId, lessonId, taskId);

        //Assert
        assertTrue(userTaskProgress.isEmpty());
        verify(this.taskAttemptGateway).findByUserIdAndTaskId(userId, taskId);
    }

    @Test
    @DisplayName("Get Task Progress - Lesson Does Not Exist - Throws Exception")
    void execute_WhenLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        String lessonId = "1", taskId = "1", userId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(false);

        // Act
        LessonNotFoundWithIdException ex = assertThrows(LessonNotFoundWithIdException.class,
                () -> this.getTaskProgress.execute(userId, lessonId, taskId));

        // Assert
        assertTrue(ex.getMessage().contains(lessonId));
    }



    // ----- HELPER METHOD SOURCES ----- //


    private static TaskProgress provideTaskProgress(String userId, String taskId) {
        return new TaskProgress("1", userId, taskId);
    }
}