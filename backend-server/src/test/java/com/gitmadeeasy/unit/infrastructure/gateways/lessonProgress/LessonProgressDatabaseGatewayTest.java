package com.gitmadeeasy.unit.infrastructure.gateways.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.LessonProgressRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
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
class LessonProgressDatabaseGatewayTest {
    @Mock private LessonProgressRepository lessonProgressRepository;
    @Mock private LessonProgressSchemaMapper lessonProgressSchemaMapper;
    @InjectMocks private LessonProgressDatabaseGateway lessonProgressDatabaseGateway;


    @Test
    @DisplayName("Save Lesson Progress - Valid Lesson Progress")
    void save_WhenValidLessonProgress_ReturnsLessonProgress() {
        // Arrange
        LessonProgress entity = provideLessonProgress();
        LessonProgressSchema schema = provideLessonProgressSchema();
        LessonProgressSchema savedSchema = provideLessonProgressSchemaWithId("1");
        LessonProgress mappedEntity = provideLessonProgressWithId("1");

        when(lessonProgressSchemaMapper.toSchema(entity)).thenReturn(schema);
        when(lessonProgressRepository.save(schema)).thenReturn(savedSchema);
        when(lessonProgressSchemaMapper.toEntity(savedSchema)).thenReturn(mappedEntity);

        // Act
        LessonProgress result = this.lessonProgressDatabaseGateway.save(entity);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getLessonProgressId());
        verify(this.lessonProgressSchemaMapper).toSchema(entity);
        verify(this.lessonProgressRepository).save(schema);
        verify(this.lessonProgressSchemaMapper).toEntity(savedSchema);
    }

    @Test
    @DisplayName("Save Lesson Progress – Repository Throws Exception")
    void save_WhenRepositoryFails_ThrowsException() {
        // Arrange
        LessonProgress entity = provideLessonProgress();
        LessonProgressSchema schema = provideLessonProgressSchema();

        when(lessonProgressSchemaMapper.toSchema(entity)).thenReturn(schema);
        when(lessonProgressRepository.save(schema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.lessonProgressDatabaseGateway.save(entity));
    }

    @Test
    @DisplayName("")
    void findByUserIdAndLessonId_WhenLessonProgressExists_ReturnsLessonProgress() {
        // Arrange
        LessonProgressSchema schema = provideLessonProgressSchemaWithId("1");
        LessonProgress mapped = provideLessonProgressWithId("1");

        when(lessonProgressRepository.findByUserIdAndLessonId("1", "1")).thenReturn(Optional.of(schema));
        when(lessonProgressSchemaMapper.toEntity(schema)).thenReturn(mapped);

        // Act
        Optional<LessonProgress> foundLessonProgress = this.lessonProgressDatabaseGateway.findByUserIdAndLessonId("1", "1");

        // Assert
        assertTrue(foundLessonProgress.isPresent());
        assertEquals("1", foundLessonProgress.get().getLessonProgressId());
        verify(this.lessonProgressSchemaMapper).toEntity(schema);
    }

    @Test
    @DisplayName("")
    void findByUserIdAndLessonId_WhenLessonProgressDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.lessonProgressRepository.findByUserIdAndLessonId("1", "1")).thenReturn(Optional.empty());

        // Act
        Optional<LessonProgress> foundLessonProgress = this.lessonProgressDatabaseGateway.findByUserIdAndLessonId("1", "1");

        // Assert
        assertTrue(foundLessonProgress.isEmpty());
        verify(this.lessonProgressSchemaMapper, never()).toEntity(any());
    }



    // ----- HELPER METHOD SOURCES ----- //


    public static LessonProgress provideLessonProgress() {
        return new LessonProgress(
                "1", "1", "1",
                null, 0, 1);
    }

    public static LessonProgress provideLessonProgressWithId(String id) {
        LessonProgress progress = provideLessonProgress();
        progress.setLessonProgressId(id);
        return progress;
    }

    public static LessonProgressSchema provideLessonProgressSchema() {
        return new LessonProgressSchema("1", "1", "1", 0, 1);
    }

    public static LessonProgressSchema provideLessonProgressSchemaWithId(String id) {
        LessonProgressSchema schema = provideLessonProgressSchema();
        schema.setLessonProgressId(Long.valueOf(id));
        return schema;
    }
}