package com.gitmadeeasy.unit.usecases.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.GetAllLessons;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllLessonsTest {
    @Mock private LessonGateway lessonGateway;
    @InjectMocks private GetAllLessons getAllLessons;


    @Test
    @DisplayName("Execute - When Lessons Exist - Returns Lessons List")
    void execute_WhenLessonsExist_ReturnsLessonsList() {
        // Arrange
        Lesson lesson1 = new Lesson("1", "Intro to Git", "Basics", DifficultyLevels.EASY, 1);
        Lesson lesson2 = new Lesson("2", "Branching", "Learn branching", DifficultyLevels.MEDIUM, 2);
        List<Lesson> expected = List.of(lesson1, lesson2);
        when(this.lessonGateway.findAllLessons()).thenReturn(expected);

        // Act
        List<Lesson> result = this.getAllLessons.execute();

        // Assert
        assertEquals(expected, result);
        verify(this.lessonGateway, times(1)).findAllLessons();
    }
    @Test
    @DisplayName("Execute - When No Lessons Exist - Returns Empty List")
    void execute_WhenNoLessonsExist_ReturnsEmptyList() {
        // Arrange
        when(this.lessonGateway.findAllLessons()).thenReturn(List.of());

        // Act
        List<Lesson> result = this.getAllLessons.execute();

        // Assert
        assertEquals(List.of(), result);
        verify(this.lessonGateway, times(1)).findAllLessons();
    }
}