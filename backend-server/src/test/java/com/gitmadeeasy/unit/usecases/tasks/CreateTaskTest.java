package com.gitmadeeasy.unit.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import com.gitmadeeasy.usecases.users.exceptions.MissingRequiredFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskTest {
    @Mock private TaskGateway taskGateway;
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private CreateTask createTask;


    @Test
    @DisplayName("Create A Task - Valid Payload")
    void execute_WhenValidPayload_ReturnsCreatedTask() {
        // Arrange
        String lessonId = "1";
        CreateTaskRequest request = provideTaskCreationRequest();
        Task createdTask = new Task("1", lessonId,
                "first git task",
                "Let's start this journey, shall we?",
                "git start",
                "easier than it may seem...", 1);
        when(this.lessonGateway.existsById(lessonId)).thenReturn(true);
        when(this.taskGateway.createTask(any(Task.class))).thenReturn(createdTask);

        // Act
        Task result = this.createTask.execute(lessonId, request);

        // Assert
        assertEquals("1", result.getTaskId());
        assertEquals("first git task", result.getTitle());
        assertEquals("Let's start this journey, shall we?", result.getContent());
        assertEquals("git start", result.getExpectedCommand());
        assertEquals("easier than it may seem...", result.getHint());
    }

    @Test
    @DisplayName("Create A Task - Task's Lesson Does Not Exist")
    void execute_WhenLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        String lessonId = "1";
        CreateTaskRequest request = provideTaskCreationRequest();
        when(this.lessonGateway.existsById(lessonId)).thenReturn(false);

        // Act
        LessonNotFoundWithIdException ex = assertThrows(LessonNotFoundWithIdException.class, () -> this.createTask.execute(lessonId, request));

        // Assert
        assertTrue(ex.getMessage().contains(String.format("lesson %s not found", lessonId)));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTaskCreationPayload")
    @DisplayName("Create A Task - Invalid Payload")
    void execute_WhenInvalidPayload_ThrowsMissingRequiredFieldException(
            String lessonId, String title, String content,
            String expectedCommand, String hint, String expectedErrorMessage) {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest(title, content, expectedCommand, hint, 1);
        when(this.lessonGateway.existsById(any(String.class))).thenReturn(true);

        // Act
        MissingRequiredFieldException ex = assertThrows(MissingRequiredFieldException.class, () -> this.createTask.execute(lessonId, request));

        // Assert
        assertEquals(expectedErrorMessage, ex.getMessage());
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static CreateTaskRequest provideTaskCreationRequest() {
        return new CreateTaskRequest(
                "first git task",
                "Let's start this journey, shall we?",
                "git start",
                "easier than it may seem...", 1
        );
    }

    private static Stream<Arguments> provideInvalidTaskCreationPayload() {
        return Stream.of(
                Arguments.of(
                        "1", null,
                        "Let's start this journey, shall we?",
                        "git start", "easier than it may seem...", "task title cannot be left blank"
                ),
                Arguments.of(
                        "1", "",
                        "Let's start this journey, shall we?",
                        "git start", "easier than it may seem...", "task title cannot be left blank"
                ),
                Arguments.of(
                        "1", " ",
                        "Let's start this journey, shall we?",
                        "git start", "easier than it may seem...", "task title cannot be left blank"
                ),

                Arguments.of(
                        "1", "first git task",
                        null,
                        "git start", "easier than it may seem...", "task content cannot be left blank"
                ),
                Arguments.of(
                        "1", "first git task",
                        "",
                        "git start", "easier than it may seem...", "task content cannot be left blank"
                ),
                Arguments.of(
                        "1", "first git task",
                        " ",
                        "git start", "easier than it may seem...", "task content cannot be left blank"
                ),

                Arguments.of(
                        "1", "first git task",
                        "Let's start this journey, shall we?",
                        null, "easier than it may seem...", "expected command cannot be left blank"
                ),
                Arguments.of(
                        "1", "first git task",
                        "Let's start this journey, shall we?",
                        "", "easier than it may seem...", "expected command cannot be left blank"
                ),
                Arguments.of(
                        "1", "first git task",
                        "Let's start this journey, shall we?",
                        " ", "easier than it may seem...", "expected command cannot be left blank"
                )
        );
    }
}