package com.gitmadeeasy.unit.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
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
class JpaLessonDatabaseGatewayTest {
    @Mock private JpaLessonRepository lessonRepository;
    @Mock private LessonSchemaMapper lessonSchemaMapper;
    @InjectMocks private JpaLessonDatabaseGateway jpaLessonDatabaseGateway;


    @Test
    @DisplayName("Create A Lesson - Valid Lesson Is Saved And Returned")
    void createLesson_WhenLessonIsValid_ReturnsSavedLesson() {
        // Arrange
        Lesson lesson = provideLesson();
        JpaLessonSchema lessonSchema = provideLessonSchema();
        when(this.lessonSchemaMapper.toJpaSchema(lesson)).thenReturn(lessonSchema);
        when(this.lessonRepository.save(lessonSchema)).thenReturn(lessonSchema);
        when(this.lessonSchemaMapper.fromJpaSchema(lessonSchema)).thenReturn(lesson);

        // Act
        Lesson createdLesson = this.jpaLessonDatabaseGateway.createLesson(lesson);

        // Assert
        assertNotNull(createdLesson);
        assertEquals(lesson.getLessonId(), createdLesson.getLessonId());
        verify(this.lessonRepository).save(lessonSchema);
        verify(this.lessonSchemaMapper).toJpaSchema(lesson);
        verify(this.lessonSchemaMapper).fromJpaSchema(lessonSchema);
    }

    @Test
    @DisplayName("Create A Lesson - Repository Throws An Exception / Fails")
    void createLesson_WhenRepositoryFails_ThrowsException() {
        // Arrange
        Lesson lesson = provideLesson();
        JpaLessonSchema lessonSchema = provideLessonSchema();
        when(this.lessonSchemaMapper.toJpaSchema(lesson)).thenReturn(lessonSchema);
        when(this.lessonRepository.save(lessonSchema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.jpaLessonDatabaseGateway.createLesson(lesson));
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists / Found")
    void getLessonById_WhenLessonExists_ReturnsLesson() {
        // Arrange
        Lesson lesson = provideLesson();
        JpaLessonSchema lessonSchema = provideLessonSchema();
        String lessonId = "1";
        when(this.lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonSchema));
        when(this.lessonSchemaMapper.fromJpaSchema(lessonSchema)).thenReturn(lesson);

        // Act
        Optional<Lesson> foundLesson = this.jpaLessonDatabaseGateway.getLessonById(lessonId);

        // Assert
        assertTrue(foundLesson.isPresent());
        assertEquals(lesson, foundLesson.get());
        verify(this.lessonSchemaMapper).fromJpaSchema(lessonSchema);
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Does Not Exist / Not Found")
    void getLessonById_WhenLessonDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.lessonRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        Optional<Lesson> foundLesson = this.jpaLessonDatabaseGateway.getLessonById(null);

        // Assert
        assertTrue(foundLesson.isEmpty());
        verify(this.lessonSchemaMapper, never()).fromJpaSchema(any(JpaLessonSchema.class));
    }

    @Test
    @DisplayName("Lesson Exists By ID - Lesson Exists - Returns True")
    void existsById_WhenLessonExists_ReturnsTrue() {
        // Arrange
        when(this.lessonRepository.existsById("1")).thenReturn(true);

        // Act
        boolean exists = this.jpaLessonDatabaseGateway.existsById("1");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Lesson Exists By ID - Lesson Does Not Exist - Returns False")
    void existsById_WhenLessonDoesNotExist_ReturnsFalse() {
        // Arrange
        when(this.lessonRepository.existsById("1")).thenReturn(false);

        // Act
        boolean exists = this.jpaLessonDatabaseGateway.existsById("1");

        // Assert
        assertFalse(exists);
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Lesson provideLesson() {
        return new Lesson("Intro to Git",
                "A simple introduction to a popular industry-relevant tool, Git.",
                DifficultyLevels.EASY);
    }

    private static JpaLessonSchema provideLessonSchema() {
        return new JpaLessonSchema(
                "Intro to Git",
                "A simple introduction to a popular industry-relevant tool, Git.",
                DifficultyLevels.EASY
        );
    }
}