package com.gitmadeeasy.unit.usecases.attemptTask;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.attemptTask.AttemptTask;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.lessonProgress.UpdateLessonProgress;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttemptTaskTest {
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @Mock private TaskGateway taskGateway;
    @Mock private UpdateLessonProgress updateLessonProgress;
    @InjectMocks private AttemptTask attemptTask;

    private static final String USER_ID = "1";
    private static final String LESSON_ID = "1";
    private static final String TASK_ID = "1";


    @Test
    @DisplayName("Record A Task Attempt - Task Not Found - Throws Exception")
    void attempt_WhenTaskDoesNotExist_ThrowsTaskNotFoundWithIdException() {
        // Arrange
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundWithIdException.class, () ->
                this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, new TaskAttemptRequest("start")));
    }

    @Test
    @DisplayName("Record A Task Attempt - Saves A New Task Attempt")
    void attempt_WhenNewTaskAttempt_ReturnsTaskProgress() {
        // Arrange
        Task task = provideTask();
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress savedTaskProgress = this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, new TaskAttemptRequest("start"));

        // Assert
        assertNotNull(savedTaskProgress);
        assertEquals("1", savedTaskProgress.getUserId());
        assertEquals("1", savedTaskProgress.getTaskId());
        verify(this.taskAttemptGateway).save(savedTaskProgress);
        verify(this.updateLessonProgress).update(USER_ID, LESSON_ID, savedTaskProgress);
    }

    @Test
    @DisplayName("Record A Task Attempt - Uses An Existing Task Attempt")
    void attempt_WhenTaskAttemptExists_ReturnsUpdatedExistingTaskProgress() {
        // Arrange
        Task task = provideTask();
        TaskProgress existingTaskProgress = provideTaskProgress();
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(existingTaskProgress));
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress updatedTaskProgress = this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, new TaskAttemptRequest("start"));

        // Assert
        assertEquals(existingTaskProgress, updatedTaskProgress);
        verify(this.taskAttemptGateway).save(existingTaskProgress);
        verify(this.updateLessonProgress).update(USER_ID, LESSON_ID, updatedTaskProgress);
    }

    @Test
    @DisplayName("Record A Task Attempt - New Task Updates Internal State")
    void attempt_WhenNewTaskAttempt_ReturnsUpdatedTaskProgress() {
        // Arrange
        Task task = provideTask();
        TaskAttemptRequest request = new TaskAttemptRequest("git start");
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress savedTaskProgress = this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request);

        // Assert
        assertEquals(USER_ID, savedTaskProgress.getUserId());
        assertEquals(TASK_ID, savedTaskProgress.getTaskId());
        assertEquals(1, savedTaskProgress.getAttempts(), "Number of attempts should be 1");
        assertTrue(savedTaskProgress.isCompleted(), "Task should be marked as completed");
        assertNull(savedTaskProgress.getLastError(), "Failed reason should be null for completed task");
    }

    @Test
    @DisplayName("Record A Task Attempt - Incorrect Input Marks Task Failed")
    void attempt_WhenNewTaskAttemptAndIncorrectInput_ReturnsFailedTaskProgress() {
        // Arrange
        Task task = provideTask();
        TaskAttemptRequest request = new TaskAttemptRequest("wrong command");
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress savedTaskProgress = this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request);

        // Assert
        assertEquals(1, savedTaskProgress.getAttempts());
        assertFalse(savedTaskProgress.isCompleted(), "Task should not be completed");
        assertEquals("Incorrect Answer", savedTaskProgress.getLastError(), "Failed reason should be set");
    }



    // ----- HELPER METHOD SOURCES ----- //


    private static Task provideTask() {
        return new Task(TASK_ID, LESSON_ID,
                "first git task",
                "Let's start this journey, shall we?",
                "git start",
                "easier than it may seem...", 1
        );
    }

    private static TaskProgress provideTaskProgress() {
        return new TaskProgress("1", USER_ID, TASK_ID);
    }
}