package com.gitmadeeasy.unit.usecases.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.GetNextLesson;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetNextLessonTest {
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private GetNextLesson getNextLesson;


    @Test
    @DisplayName("Get Next Lesson - Current Lesson ID Is Valid And Next Lesson Exists")
    void execute_WhenNextLessonExistsForCurrentLesson_ReturnsLesson() {
        // Arrange
        Lesson lesson1 = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        Lesson lesson2 = new Lesson("2", "Intro 2", "Description 2", DifficultyLevels.EASY, 2);
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.of(lesson1));
        when(this.lessonGateway.getNextLesson(lesson1.getLessonOrder())).thenReturn(lesson2);

        // Act
        Lesson nextLesson = this.getNextLesson.execute("1");

        // Assert
        assertEquals(2, nextLesson.getLessonOrder());
        assertEquals("2", nextLesson.getLessonId());
    }

    @Test
    @DisplayName("Get Next Lesson - No Next Lesson Exists")
    void execute_WhenNoNextLessonExists_ReturnsNull() {
        // Arrange
        Lesson lesson1 = new Lesson("1", "Intro", "Description", DifficultyLevels.EASY, 1);
        when(this.lessonGateway.getLessonById("1")).thenReturn(Optional.of(lesson1));
        when(this.lessonGateway.getNextLesson(1)).thenReturn(null);

        // Act & Assert
        assertNull(this.getNextLesson.execute("1"));
    }

    @Test
    @DisplayName("Get Next Lesson - When Current Lesson Does Not Exist - Throws Exception")
    void execute_WhenCurrentLessonDoesNotExist_ThrowsLessonNotFoundWithIdException() {
        // Arrange
        when(this.lessonGateway.getLessonById("1")).thenThrow(new LessonNotFoundWithIdException("1"));

        // Act & Assert
        assertThrows(LessonNotFoundWithIdException.class, () -> this.getNextLesson.execute("1"));
    }
}