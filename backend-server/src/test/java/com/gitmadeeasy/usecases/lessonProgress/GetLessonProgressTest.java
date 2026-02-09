package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLessonProgressTest {
    @Mock private LessonProgressGateway lessonProgressGateway;
    @InjectMocks private GetLessonProgress getLessonProgress;


    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exists")
    void execute_WhenLessonProgressExists_ReturnsLessonProgress() {
        // Arrange
        String userId = "1", lessonId = "1";
        LessonProgress lessonProgress = new LessonProgress(
                "1", userId, lessonId,
                "1", 0, 1);
        when(this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.of(lessonProgress));

        // Act
        Optional<LessonProgress> foundLessonProgress = this.getLessonProgress.execute(userId, lessonId);

        // Assert
        assertTrue(foundLessonProgress.isPresent());
        assertEquals(lessonProgress, foundLessonProgress.get());
        verify(this.lessonProgressGateway, times(1)).findByUserIdAndLessonId(userId, lessonId);
    }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Does Not Exist")
    void execute_WhenLessonProgressDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        String userId = "1", lessonId = "1";
        when(this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId)).thenReturn(Optional.empty());

        // Act
        Optional<LessonProgress> foundLessonProgress = this.getLessonProgress.execute(userId, lessonId);

        // Assert
        assertTrue(foundLessonProgress.isEmpty());
        verify(this.lessonProgressGateway, times(1)).findByUserIdAndLessonId(userId, lessonId);
    }
}