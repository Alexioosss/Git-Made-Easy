package com.gitmadeeasy.unit.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
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

import java.util.List;
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
    @DisplayName("Find All Lessons - Lessons Exist - Returns Mapped Lessons")
    void findAllLessons_WhenLessonsExist_ReturnsMappedLessons() {
        // Arrange
        JpaLessonSchema schema1 = provideLessonSchema();
        JpaLessonSchema schema2 = provideLessonSchema();
        Lesson lesson1 = provideLesson();
        Lesson lesson2 = provideLesson();
        when(this.lessonRepository.findAllByOrderByLessonOrderAsc()).thenReturn(List.of(schema1, schema2));
        when(this.lessonSchemaMapper.fromJpaSchema(schema1)).thenReturn(lesson1);
        when(this.lessonSchemaMapper.fromJpaSchema(schema2)).thenReturn(lesson2);

        // Act
        List<Lesson> result = this.jpaLessonDatabaseGateway.findAllLessons();

        // Assert
        assertEquals(List.of(lesson1, lesson2), result);
        verify(this.lessonRepository).findAllByOrderByLessonOrderAsc();
        verify(this.lessonSchemaMapper).fromJpaSchema(schema1);
        verify(this.lessonSchemaMapper).fromJpaSchema(schema2);
    }

    @Test
    @DisplayName("Find All Lessons - No Lessons Exist - Returns Empty List")
    void findAllLessons_WhenNoLessonsExist_ReturnsEmptyList() {
        // Arrange
        when(this.lessonRepository.findAllByOrderByLessonOrderAsc()).thenReturn(List.of());

        // Act
        List<Lesson> result = this.jpaLessonDatabaseGateway.findAllLessons();

        // Assert
        assertTrue(result.isEmpty());
        verify(this.lessonRepository).findAllByOrderByLessonOrderAsc();
        verify(this.lessonSchemaMapper, never()).fromJpaSchema(any());
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

    // FROM HERE
    @Test
    @DisplayName("Get Next Lesson - Next Lesson Exists - Returns Mapped Lesson")
    void getNextLesson_WhenNextLessonExists_ReturnsLesson() {
        // Arrange
        int currentOrder = 1;

        JpaLessonSchema nextSchema = provideLessonSchema();
        Lesson nextLesson = provideLesson();

        when(this.lessonRepository.findNextLesson(currentOrder)).thenReturn(Optional.of(nextSchema));
        when(this.lessonSchemaMapper.fromJpaSchema(nextSchema)).thenReturn(nextLesson);

        // Act
        Lesson result = this.jpaLessonDatabaseGateway.getNextLesson(currentOrder);

        // Assert
        assertNotNull(result);
        assertEquals(nextLesson, result);
        verify(this.lessonRepository).findNextLesson(currentOrder);
        verify(this.lessonSchemaMapper).fromJpaSchema(nextSchema);
    }

    @Test
    @DisplayName("Get Next Lesson - No Next Lesson Exists - Returns Null")
    void getNextLesson_WhenNoNextLessonExists_ReturnsNull() {
        // Arrange
        int currentOrder = 1;
        when(this.lessonRepository.findNextLesson(currentOrder)).thenReturn(Optional.empty());

        // Act
        Lesson result = this.jpaLessonDatabaseGateway.getNextLesson(currentOrder);

        // Assert
        assertNull(result);
        verify(this.lessonRepository).findNextLesson(currentOrder);
        verify(this.lessonSchemaMapper, never()).fromJpaSchema(any());
    }

    @Test
    @DisplayName("Get Next Lesson Order - Lessons Exist - ReturnsMaxPlusOne")
    void getNextLessonOrder_WhenLessonsExist_ReturnsNextOrder() {
        // Arrange
        when(this.lessonRepository.findMaxLessonOrder()).thenReturn(5);

        // Act
        Integer nextOrder = this.jpaLessonDatabaseGateway.getNextLessonOrder();

        // Assert
        assertEquals(6, nextOrder);
        verify(this.lessonRepository).findMaxLessonOrder();
    }

    @Test
    @DisplayName("Get Next Lesson Order - No Lessons Exist - Returns 1")
    void getNextLessonOrder_WhenNoLessonsExist_ReturnsOne() {
        // Arrange
        when(this.lessonRepository.findMaxLessonOrder()).thenReturn(0);

        // Act
        Integer nextOrder = this.jpaLessonDatabaseGateway.getNextLessonOrder();

        // Assert
        assertEquals(1, nextOrder);
        verify(this.lessonRepository).findMaxLessonOrder();
    }



    // ----------  HELPER METHODS FOR PARAMETERISED TESTS ---------- //


    private static Lesson provideLesson() {
        return new Lesson("Intro to Git",
                "A simple introduction to a popular industry-relevant tool, Git.",
                DifficultyLevels.EASY, 1);
    }

    private static JpaLessonSchema provideLessonSchema() {
        return new JpaLessonSchema(
                "Intro to Git","A simple introduction to a popular industry-relevant tool, Git.",
                DifficultyLevels.EASY, 1);
    }
}