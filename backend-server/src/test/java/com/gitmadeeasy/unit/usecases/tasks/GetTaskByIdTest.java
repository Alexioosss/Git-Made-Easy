package com.gitmadeeasy.unit.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
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
class GetTaskByIdTest {
    @Mock private TaskGateway taskGateway;
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private GetTaskById getTaskById;


    @Test
    @DisplayName("Get Task By ID - Lesson And Task Exist - Returns Task")
    void execute_WhenLessonAndTaskExist_ReturnsTask() {
        // Arrange
        String lessonId = "1", taskId = "1";
        Task task = provideTask();
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId))
                .thenReturn(Optional.of(task));

        // Act
        Task foundTask = this.getTaskById.execute(lessonId, taskId);

        // Assert
        assertEquals("1", foundTask.getTaskId());
        assertEquals("1", foundTask.getLessonId());
        assertEquals(task, foundTask);
        verify(this.taskGateway).getTaskByLessonIdAndTaskId(lessonId, taskId);
    }

    @Test
    @DisplayName("Get Task By ID - Lesson Does Not Exist - Throws Exception")
    void execute_WhenLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        String lessonId = "1", taskId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(false);

        // Act
        LessonNotFoundWithIdException ex = assertThrows(LessonNotFoundWithIdException.class,
                () -> this.getTaskById.execute(lessonId, taskId));

        // Assert
        assertTrue(ex.getMessage().contains(lessonId));
    }

    @Test
    @DisplayName("Get Task By ID - Task Does Not Exist - Throws Exception")
    void execute_WhenTaskDoesNotExist_ThrowsTaskNotFoundWithIdException() {
        // Arrange
        String lessonId = "1", taskId = "1";
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId))
                .thenReturn(Optional.empty());

        // Act
        TaskNotFoundWithIdException ex = assertThrows(TaskNotFoundWithIdException.class,
                () -> this.getTaskById.execute(lessonId, taskId));

        // Assert
        assertTrue(ex.getMessage().contains(taskId));
    }



    // ----- HELPER METHOD SOURCES ----- //


    private static Task provideTask() {
        return new Task(
                "1", "1",
                "first git task",
                "Let's start this journey, shall we?",
                "git start",
                "easier than it may seem...", 1
        );
    }
}