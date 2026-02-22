package com.gitmadeeasy.unit.usecases.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLessonByIdTest {
    @Mock private LessonGateway lessonGateway;
    @Mock private TaskGateway taskGateway;
    @InjectMocks private GetLessonById getLessonById;


    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists")
    void execute_WhenLessonExists_ReturnsLesson() {
        // Arrange
        Lesson lesson = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        lesson.setTaskIds(List.of());
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.of(lesson));
        when(this.taskGateway.getTasksByIds(List.of())).thenReturn(List.of());

        // Act
        Lesson result = this.getLessonById.execute("1");

        // Assert
        assertEquals("Intro", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertEquals(DifficultyLevels.EASY, result.getDifficulty());
        verify(this.taskGateway).getTasksByIds(List.of());
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Does Not Exist")
    void execute_WhenLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LessonNotFoundWithIdException.class, () -> this.getLessonById.execute("1"));
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists With No Tasks")
    void execute_WhenLessonExistsWithNoTasks_ReturnsLessonWithEmptyTasksList() {
        // Arrange
        Lesson lesson = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        lesson.setTaskIds(List.of());
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.of(lesson));
        when(this.taskGateway.getTasksByIds(List.of())).thenReturn(List.of());

        // Act
        Lesson result = this.getLessonById.execute("1");

        // Assert
        assertEquals(List.of(), result.getTasks());
        verify(this.taskGateway).getTasksByIds(List.of());
    }

    @ParameterizedTest
    @MethodSource("provideValidTasksList")
    @DisplayName("Get Lesson By ID - Lesson Exists With Tasks")
    void execute_WhenLessonExistsWithTasks_ReturnsLessonWithTasks(List<Task> tasks) {
        // Arrange
        Lesson lesson = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        lesson.setTaskIds(List.of("1", "2"));
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.of(lesson));
        when(this.taskGateway.getTasksByIds(List.of("1", "2"))).thenReturn(tasks);

        // Act
        Lesson result = this.getLessonById.execute("1");

        // Assert
        assertEquals(tasks.size(), result.getTasks().size());
        verify(this.taskGateway).getTasksByIds(List.of("1", "2"));
    }



    private static Stream<List<Task>> provideValidTasksList() {
        return Stream.of(
                List.of(
                        new Task("1", "1", "Task 1",
                                "Content", "git", "easy", 1, DifficultyLevels.EASY),
                        new Task("2", "1", "Task 2",
                                "Content", "git", "easy", 2, DifficultyLevels.EASY)
                )
        );
    }
}