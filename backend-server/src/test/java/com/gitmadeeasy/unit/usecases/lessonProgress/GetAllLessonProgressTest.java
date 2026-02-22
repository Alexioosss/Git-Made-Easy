package com.gitmadeeasy.unit.usecases.lessonProgress;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessonProgress.GetAllLessonProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllLessonProgressTest {
    @Mock private LessonProgressGateway lessonProgressGateway;
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private GetAllLessonProgress getAllLessonProgress;


    @Test
    @DisplayName("Execute - Returns Sorted Lesson Progress By Lesson Order")
    void execute_WhenLessonsExist_ReturnsSortedList() {
        // Arrange
        LessonProgress p1 = new LessonProgress(
                "1", "1", "Lesson-1", null, 0, 1);
        LessonProgress p2 = new LessonProgress(
                "2", "1", "Lesson-2", null, 0, 1);

        when(this.lessonProgressGateway.findAllByUserId("1")).thenReturn(List.of(p1, p2));
        when(this.lessonGateway.getLessonById("Lesson-1")).thenReturn(Optional.of(new Lesson(
                        "Lesson-1", "Title", "Description", DifficultyLevels.EASY, 1)));
        when(this.lessonGateway.getLessonById("Lesson-2")).thenReturn(Optional.of(new Lesson(
                        "Lesson-2", "Title", "Description", DifficultyLevels.EASY, 1)));

        // Act
        List<LessonProgress> result = this.getAllLessonProgress.execute("1");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Lesson-1", result.get(0).getLessonId());
        assertEquals("Lesson-2", result.get(1).getLessonId());
    }

    @Test
    @DisplayName("Execute - Returns Empty List When No Progress Exists")
    void execute_WhenNoProgressExists_ReturnsEmptyList() {
        // Arrange
        when(this.lessonProgressGateway.findAllByUserId("1")).thenReturn(List.of());

        // Act
        List<LessonProgress> result = this.getAllLessonProgress.execute("1");

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Execute - Missing Lesson Defaults To MAX_VALUE Order")
    void execute_WhenLessonMissing_UsesMaxValueOrder() {
        // Arrange
        LessonProgress p1 = new LessonProgress(
                "1", "1", "Lesson-1", null, 0, 1);
        LessonProgress p2 = new LessonProgress(
                "2", "1", "Lesson-0", null, 0, 1);

        when(this.lessonProgressGateway.findAllByUserId("1")).thenReturn(List.of(p2, p1));
        when(this.lessonGateway.getLessonById("Lesson-1")).thenReturn(Optional.of(
                new Lesson("Lesson-1", "Title", "Description", DifficultyLevels.EASY, 1)));
        when(this.lessonGateway.getLessonById("Lesson-0")).thenReturn(Optional.empty());

        // Act
        List<LessonProgress> result = this.getAllLessonProgress.execute("1");

        // Assert
        assertEquals("Lesson-1", result.get(0).getLessonId());
        assertEquals("Lesson-0", result.get(1).getLessonId());
    }
}