package com.gitmadeeasy.usecases.taskAttempt;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.taskAttempt.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotInLessonException;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.MinValidatorForBigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskAttemptTest {
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @Mock private LessonGateway lessonGateway;
    @Mock private TaskGateway taskGateway;
    private TaskAttempt taskAttempt;

    @BeforeEach
    void setUp() { this.taskAttempt = new TaskAttempt(taskAttemptGateway, lessonGateway, taskGateway); }

    @Test
    @DisplayName("Record A Task Attempt - Task Exist And Correct Answer - Returns Completed TaskProgress")
    void execute_WhenTaskExistsAndCorrectAnswer_ReturnsCompletedTaskProgress() {
        // Arrange
        String userId = "1", lessonId = "1", taskId = "1";
        TaskAttemptRequest request = new TaskAttemptRequest("git start");
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        Task task = provideTask(taskId, lessonId);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(null);
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress result = this.taskAttempt.execute(userId, lessonId, taskId, request);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(taskId, result.getTaskId());
        assertEquals(1, result.getAttempts());
        assertEquals("git start", result.getLastInput());
        assertEquals(TaskCompletionStatus.COMPLETED, result.getStatus());
    }

    @Test
    @DisplayName("Record A Task Attempt - Task Exist And Incorrect Answer - Returns Failed TaskProgress")
    void execute_WhenTaskExistsAndIncorrectAnswer_ReturnsFailedTaskProgress() {
        // Arrange
        String userId = "1", lessonId = "1", taskId = "1";
        TaskAttemptRequest request = new TaskAttemptRequest("start");
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        Task task = provideTask(taskId, lessonId);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)).thenReturn(Optional.of(task));
        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(null);
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress result = this.taskAttempt.execute(userId, lessonId, taskId, request);

        // Assert
        assertEquals("start", result.getLastInput());
        assertEquals("Incorrect Answer", result.getLastError());
        assertEquals(TaskCompletionStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    @DisplayName("Record A Task Attempt - Task Already Completed - Returns Same TaskProgress - Unchanged Status")
    void execute_WhenTaskAlreadyCompleted_ReturnsSameTaskProgress() {
        // Arrange
        String userId = "1", lessonId = "1", taskId = "1";
        TaskAttemptRequest request = new TaskAttemptRequest("git start");
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        Task task = provideTask(taskId, lessonId);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)).thenReturn(Optional.of(task));
        TaskProgress existing = provideTaskProgress("1", userId, taskId);
        existing.markCompleted();

        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(existing);
        when(this.taskAttemptGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskProgress result = this.taskAttempt.execute(userId, lessonId, taskId, request);

        // Assert
        assertEquals("git start", result.getLastInput());
        assertEquals(1, result.getAttempts());
        assertEquals(TaskCompletionStatus.COMPLETED, result.getStatus());
    }

    @Test
    @DisplayName("Record A Task Attempt - Task Not Found - Throws Exception")
    void execute_WhenTaskDoesNotExist_ThrowsTaskNotFoundWithIdException() {
        // Arrange
        when(this.lessonGateway.existsById("1")).thenReturn(true);
        when(this.taskGateway.getTaskByLessonIdAndTaskId("1", "1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundWithIdException.class, () -> this.taskAttempt.execute(
                "1", "1", "1", new TaskAttemptRequest("start")));
    }

    @Test
    @DisplayName("Record A Task Attempt - Lesson Not Found - Throws Exception")
    void execute_WhenLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        when(this.lessonGateway.existsById("1")).thenReturn(false);

        // Act & Assert
        assertThrows(LessonNotFoundWithIdException.class, () -> this.taskAttempt.execute(
                "1", "1", "1", new TaskAttemptRequest("start")));
    }

    @Test
    @DisplayName("Record A Task Attempt - Task Not In Lesson - Throws Exception")
    void execute_WhenTaskNotInLesson_ThrowsException() {
        // Arrange
        when(this.lessonGateway.existsById("1")).thenReturn(true);
        Task task = provideTask("1", "2");
        when(this.taskGateway.getTaskByLessonIdAndTaskId("1", "1"))
                .thenReturn(Optional.of(task));

        // Act & Assert
        assertThrows(TaskNotInLessonException.class, () -> this.taskAttempt.execute(
                "1", "1", "1", new TaskAttemptRequest("start")
        ));
    }

    @Test
    @DisplayName("Record A Task Attempt - Multiple Attempts - Attempts List Increases Correctly")
    void execute_WhenMultipleAttempts_AttemptsIncreaseCorrectly() {
        // Arrange
        String userId = "1", lessonId = "1", taskId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        Task task = provideTask(taskId, lessonId);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId))
                .thenReturn(Optional.of(task));

        // Existing user task progress with a failed attempt
        TaskProgress existing = provideTaskProgress("1", userId, taskId);
        existing.recordAttempt("init");
        existing.markFailed("Incorrect Answer");

        when(this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)).thenReturn(existing);
        when(this.taskAttemptGateway.save(any()))
                .thenAnswer(invocation -> cloneOf(invocation.getArgument(0)));

        // Act
        TaskAttemptRequest attempt2 = new TaskAttemptRequest("start");
        TaskProgress afterSecondAttempt = this.taskAttempt.execute(userId, lessonId, taskId, attempt2);

        TaskAttemptRequest attempt3 = new TaskAttemptRequest("git start");
        TaskProgress afterThirdAttempt = this.taskAttempt.execute(userId, lessonId, taskId, attempt3);


        // Assert
        // After 2nd Attempt
        assertEquals(2, afterSecondAttempt.getAttempts());
        assertEquals("start", afterSecondAttempt.getLastInput());
        assertEquals("Incorrect Answer", afterSecondAttempt.getLastError());
        assertEquals(TaskCompletionStatus.IN_PROGRESS, afterSecondAttempt.getStatus());

        // After 3rd Attempt
        assertEquals(3, afterThirdAttempt.getAttempts());
        assertEquals("git start", afterThirdAttempt.getLastInput());
        assertEquals(TaskCompletionStatus.COMPLETED, afterThirdAttempt.getStatus());
        assertNull(afterThirdAttempt.getLastError());
        assertNotNull(afterThirdAttempt.getCompletedAt());
    }



    // ----- HELPER METHOD SOURCES ----- //


    private static Task provideTask(String taskId, String lessonId) {
        return new Task(taskId, lessonId,
                "first git task",
                "Let's start this journey, shall we?",
                "git start",
                "easier than it may seem..."
        );
    }

    private static TaskProgress provideTaskProgress(String taskProgressId, String userId, String taskId) {
        return new TaskProgress(taskProgressId, userId, taskId);
    }

    private TaskProgress cloneOf(TaskProgress original) {
        TaskProgress copy = new TaskProgress(original.getTaskProgressId(), original.getUserId(), original.getTaskId());
        copy.setAttempts(original.getAttempts());
        copy.setStatus(original.getStatus());
        copy.setLastInput(original.getLastInput());
        copy.setLastError(original.getLastError());
        copy.setStartedAt(original.getStartedAt());
        copy.setCompletedAt(original.getCompletedAt());

        return copy;
    }
}