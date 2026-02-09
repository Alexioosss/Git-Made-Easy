package com.gitmadeeasy.unit.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonDatabaseGatewayTest {
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonSchemaMapper lessonSchemaMapper;
    @InjectMocks private LessonDatabaseGateway lessonDatabaseGateway;


    @Test
    @DisplayName("Create A Lesson - Valid Lesson Is Saved And Returned")
    void createLesson_WhenLessonIsValid_ReturnsSavedLesson() {
        // Arrange
        Lesson lesson = provideLesson();
        LessonSchema lessonSchema = provideLessonSchema();
        when(this.lessonSchemaMapper.toSchema(lesson)).thenReturn(lessonSchema);
        when(this.lessonRepository.save(lessonSchema)).thenReturn(lessonSchema);
        when(this.lessonSchemaMapper.toEntity(lessonSchema)).thenReturn(lesson);

        // Act
        Lesson createdLesson = this.lessonDatabaseGateway.createLesson(lesson);

        // Assert
        assertNotNull(createdLesson);
        assertEquals(lesson.getLessonId(), createdLesson.getLessonId());
        verify(this.lessonRepository).save(lessonSchema);
        verify(this.lessonSchemaMapper).toSchema(lesson);
        verify(this.lessonSchemaMapper).toEntity(lessonSchema);
    }

    @Test
    @DisplayName("Create A Lesson - Repository Throws An Exception / Fails")
    void createLesson_WhenRepositoryFails_ThrowsException() {
        // Arrange
        Lesson lesson = provideLesson();
        LessonSchema lessonSchema = provideLessonSchema();
        when(this.lessonSchemaMapper.toSchema(lesson)).thenReturn(lessonSchema);
        when(this.lessonRepository.save(lessonSchema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.lessonDatabaseGateway.createLesson(lesson));
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists / Found")
    void getLessonById_WhenLessonExists_ReturnsLesson() {
        // Arrange
        Lesson lesson = provideLesson();
        LessonSchema lessonSchema = provideLessonSchema();
        String lessonId = "1";
        when(this.lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonSchema));
        when(this.lessonSchemaMapper.toEntity(lessonSchema)).thenReturn(lesson);

        // Act
        Optional<Lesson> foundLesson = this.lessonDatabaseGateway.getLessonById(lessonId);

        // Assert
        assertTrue(foundLesson.isPresent());
        assertEquals(lesson, foundLesson.get());
        verify(this.lessonSchemaMapper).toEntity(lessonSchema);
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Does Not Exist / Not Found")
    void getLessonById_WhenLessonDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.lessonRepository.findById(null)).thenReturn(Optional.empty());

        // Act
        Optional<Lesson> foundLesson = this.lessonDatabaseGateway.getLessonById(null);

        // Assert
        assertTrue(foundLesson.isEmpty());
        verify(this.lessonSchemaMapper, never()).toEntity(any(LessonSchema.class));
    }

    @Test
    @DisplayName("Lesson Exists By ID - Lesson Exists - Returns True")
    void existsById_WhenLessonExists_ReturnsTrue() {
        // Arrange
        when(this.lessonRepository.existsById("1")).thenReturn(true);

        // Act
        boolean exists = this.lessonDatabaseGateway.existsById("1");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Lesson Exists By ID - Lesson Does Not Exist - Returns False")
    void existsById_WhenLessonDoesNotExist_ReturnsFalse() {
        // Arrange
        when(this.lessonRepository.existsById("1")).thenReturn(false);

        // Act
        boolean exists = this.lessonDatabaseGateway.existsById("1");

        // Assert
        assertFalse(exists);
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Lesson provideLesson() {
        return new Lesson("Intro to Git",
                "A simple introduction to a popular industry-relevant tool, Git.",
                LessonDifficulty.EASY);
    }

    private static LessonSchema provideLessonSchema() {
        return new LessonSchema(
                "Intro to Git",
                "A simple introduction to a popular industry-relevant tool, Git.",
                LessonDifficulty.EASY
        );
    }
}