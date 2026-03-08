package com.gitmadeeasy.unit.usecases.taskProgress;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskProgress.GetTaskProgress;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTaskProgressTest {
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private GetTaskProgress getTaskProgress;


    @Test
    @DisplayName("Get Task Progress - Lesson, Task And User Task Progress Exist - Returns The User's Task Progress")
    void execute_WhenLessonAndTaskAndUserTaskProgressExist_ReturnsTaskProgress() {
        // Arrange
        String lessonId = "1";
        String taskId = "1";
        String userId = "1";
        TaskProgress taskProgress = provideTaskProgress(userId, taskId);
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(taskProgress));

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
        String lessonId = "1";
        String taskId = "1";
        String userId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        // Mock the data store's return of a not-found entity
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.empty());

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
        String lessonId = "1";
        String taskId = "1";
        String userId = "1";
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