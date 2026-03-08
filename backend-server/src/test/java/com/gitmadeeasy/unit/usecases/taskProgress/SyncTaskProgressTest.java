package com.gitmadeeasy.unit.usecases.taskProgress;

import com.gitmadeeasy.entities.enums.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessonProgress.LessonProgressFacade;
import com.gitmadeeasy.usecases.taskProgress.SyncTaskProgress;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskProgressUpdateRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SyncTaskProgressTest {
    @Mock private TaskAttemptGateway taskProgressGateway;
    @Mock private TaskGateway taskGateway;
    @Mock private LessonProgressFacade lessonProgressFacade;

    @InjectMocks
    private SyncTaskProgress syncTaskProgress;

    private static final String USER_ID = "1";
    private static final String LESSON_ID = "10";
    private static final String TASK_ID = "5";


    @Test
    @DisplayName("Sync Progress - Task Not Found - Throws Exception")
    void syncProgress_WhenTaskNotFound_ThrowsException() {
        // Arrange
        TaskProgressUpdateRequest update = new TaskProgressUpdateRequest(
                TASK_ID, "COMPLETED", 1, "input", null,
                "2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z");
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundWithIdException.class, () -> this.syncTaskProgress.syncProgress(USER_ID, LESSON_ID, List.of(update)));
    }

    @Test
    @DisplayName("Sync Progress - Creates New TaskProgress When No Progress Exists")
    void syncProgress_WhenNoExistingProgressExists_CreatesNewProgress() {
        // Arrange
        Task task = provideTask();
        TaskProgressUpdateRequest update = provideUpdateRequest();
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskProgressGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.empty());
        when(this.taskProgressGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<TaskProgress> results = this.syncTaskProgress.syncProgress(USER_ID, LESSON_ID, List.of(update));
        TaskProgress saved = results.get(0);

        // Assert
        assertEquals(USER_ID, saved.getUserId());
        assertEquals(TASK_ID, saved.getTaskId());
        assertEquals(TaskCompletionStatus.COMPLETED, saved.getStatus());
        assertEquals(3, saved.getAttempts());
        assertEquals("git start", saved.getLastInput());
        assertEquals("", saved.getLastError());
        assertEquals(LocalDate.parse("2024-01-01"), saved.getStartedAt());
        assertEquals(LocalDate.parse("2024-01-02"), saved.getCompletedAt());
        verify(this.taskProgressGateway).save(saved);
        verify(this.lessonProgressFacade).update(USER_ID, LESSON_ID, saved);
    }

    @Test
    @DisplayName("Sync Progress - Updates Existing TaskProgress")
    void syncProgress_WhenExistingProgress_UpdatesIt() {
        // Arrange
        Task task = provideTask();
        TaskProgress existing = provideExistingProgress();
        TaskProgressUpdateRequest update = provideUpdateRequest();
        when(this.taskGateway.getTaskByLessonIdAndTaskId(LESSON_ID, TASK_ID)).thenReturn(Optional.of(task));
        when(this.taskProgressGateway.findByUserIdAndTaskId(USER_ID, TASK_ID)).thenReturn(Optional.of(existing));
        when(this.taskProgressGateway.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        List<TaskProgress> results = this.syncTaskProgress.syncProgress(USER_ID, LESSON_ID, List.of(update));
        TaskProgress saved = results.get(0);

        // Assert
        assertEquals(existing.getTaskProgressId(), saved.getTaskProgressId());
        assertEquals(TaskCompletionStatus.COMPLETED, saved.getStatus());
        assertEquals(3, saved.getAttempts());
        verify(this.taskProgressGateway).save(saved);
        verify(this.lessonProgressFacade).update(USER_ID, LESSON_ID, saved);
    }



    // ----- HELPER METHODS ----- //


    private Task provideTask() {
        return new Task(TASK_ID, LESSON_ID, "title", "desc", "cmd", "hint", 1, null);
    }

    private TaskProgressUpdateRequest provideUpdateRequest() {
        return new TaskProgressUpdateRequest(
                TASK_ID,
                "COMPLETED",
                3,
                "git start",
                null,
                "2024-01-01T00:00:00Z",
                "2024-01-02T00:00:00Z");
    }

    private TaskProgress provideExistingProgress() {
        return new TaskProgress("existing-id", USER_ID, TASK_ID);
    }
}