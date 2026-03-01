package com.gitmadeeasy.unit.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessonProgress.LessonProgressFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonProgressFacadeTest {
    @Mock private LessonProgressGateway lessonProgressGateway;
    @Mock private TaskAttemptGateway taskAttemptGateway;
    @Mock private TaskGateway taskGateway;
    @InjectMocks private LessonProgressFacade lessonProgressFacade;

    private static final String USER_ID = "1";
    private static final String LESSON_ID = "1";

    private TaskProgress completedTaskProgress;
    private TaskProgress inProgressTaskProgress;

    @BeforeEach
    void setUp() {
        completedTaskProgress = new TaskProgress("1", USER_ID, "1");
        completedTaskProgress.markCompleted();

        inProgressTaskProgress = new TaskProgress("2", USER_ID, "1");
        inProgressTaskProgress.markFailed("Incorrect answer");
    }


    @Test
    @DisplayName("Update - Existing Lesson Progress With Completed Task")
    void update_WhenExistingLessonProgressAndTaskCompleted_UpdatesCounts() {
        // Arrange
        LessonProgress existingLessonProgress = new LessonProgress("1", USER_ID, LESSON_ID, "1", 0, 3);
        when(this.lessonProgressGateway.findByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(Optional.of(existingLessonProgress));
        when(this.taskAttemptGateway.countCompletedTasks(USER_ID, LESSON_ID)).thenReturn(1);
        when(this.taskGateway.countTasksInLesson(LESSON_ID)).thenReturn(3);

        // Act
        this.lessonProgressFacade.update(USER_ID, LESSON_ID, completedTaskProgress);

        // Assert
        assertEquals(completedTaskProgress.getTaskProgressId(), existingLessonProgress.getCurrentTaskProgressId());
        assertEquals(1, existingLessonProgress.getCompletedTasksCount());
        assertEquals(3, existingLessonProgress.getTotalTasksCount());
        verify(this.lessonProgressGateway).save(existingLessonProgress);
    }

    @Test
    @DisplayName("Update - Existing Lesson Progress With IN-PROGRESS Task")
    void update_WhenExistingLessonProgressAndTaskInProgress_UpdatesCounts() {
        // Arrange
        LessonProgress existing = new LessonProgress("lp1", USER_ID, LESSON_ID, "oldTask", 1, 3);
        when(this.lessonProgressGateway.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                .thenReturn(Optional.of(existing));
        when(this.taskAttemptGateway.countCompletedTasks(USER_ID, LESSON_ID)).thenReturn(2);
        when(this.taskGateway.countTasksInLesson(LESSON_ID)).thenReturn(3);

        // Act
        this.lessonProgressFacade.update(USER_ID, LESSON_ID, inProgressTaskProgress);

        // Assert
        assertEquals(inProgressTaskProgress.getTaskProgressId(), existing.getCurrentTaskProgressId());
        assertEquals(2, existing.getCompletedTasksCount());
        assertEquals(3, existing.getTotalTasksCount());
        verify(this.lessonProgressGateway).save(existing);
    }

    @Test
    @DisplayName("Update - Existing Lesson Progress With NOT-STARTED Task")
    void update_WhenNoExistingLessonProgressAndTaskCompleted_ReturnsNewProgress() {
        // Arrange
        when(this.lessonProgressGateway.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                .thenReturn(Optional.empty());
        when(this.taskGateway.countTasksInLesson(LESSON_ID)).thenReturn(3);
        when(this.lessonProgressGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        this.lessonProgressFacade.update(USER_ID, LESSON_ID, completedTaskProgress);

        // Assert
        verify(this.lessonProgressGateway).save(any(LessonProgress.class));
    }

    @Test
    @DisplayName("Update - New Lesson Progress For IN-PROGRESS Task")
    void update_WhenNoExistingLessonProgressAndTaskInProgress_ReturnsNewTaskProgress() {
        // Arrange
        when(this.lessonProgressGateway.findByUserIdAndLessonId(USER_ID, LESSON_ID))
                .thenReturn(Optional.empty());
        when(this.taskGateway.countTasksInLesson(LESSON_ID)).thenReturn(3);
        when(this.lessonProgressGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        this.lessonProgressFacade.update(USER_ID, LESSON_ID, inProgressTaskProgress);

        // Assert
        verify(this.lessonProgressGateway).save(any(LessonProgress.class));
    }
}