package com.gitmadeeasy.unit.infrastructure.gateways.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressDatabaseGateway;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressSchema;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.JpaLessonProgressRepository;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
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
class JpaLessonProgressDatabaseGatewayTest {
    @Mock private JpaLessonProgressRepository lessonProgressRepository;
    @Mock private LessonProgressSchemaMapper lessonProgressSchemaMapper;
    @InjectMocks private JpaLessonProgressDatabaseGateway jpaLessonProgressDatabaseGateway;


    @Test
    @DisplayName("Save Lesson Progress - Valid Lesson Progress")
    void save_WhenValidLessonProgress_ReturnsLessonProgress() {
        // Arrange
        LessonProgress entity = provideLessonProgress();
        JpaLessonProgressSchema schema = provideLessonProgressSchema();
        JpaLessonProgressSchema savedSchema = provideLessonProgressSchemaWithId("1");
        LessonProgress mappedEntity = provideLessonProgressWithId("1");

        when(this.lessonProgressSchemaMapper.toJpaSchema(entity)).thenReturn(schema);
        when(this.lessonProgressRepository.save(schema)).thenReturn(savedSchema);
        when(this.lessonProgressSchemaMapper.fromJpaSchema(savedSchema)).thenReturn(mappedEntity);

        // Act
        LessonProgress result = this.jpaLessonProgressDatabaseGateway.save(entity);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getLessonProgressId());
        verify(this.lessonProgressSchemaMapper).toJpaSchema(entity);
        verify(this.lessonProgressRepository).save(schema);
        verify(this.lessonProgressSchemaMapper).fromJpaSchema(savedSchema);
    }

    @Test
    @DisplayName("Save Lesson Progress – Repository Throws Exception")
    void save_WhenRepositoryFails_ThrowsException() {
        // Arrange
        LessonProgress entity = provideLessonProgress();
        JpaLessonProgressSchema schema = provideLessonProgressSchema();

        when(this.lessonProgressSchemaMapper.toJpaSchema(entity)).thenReturn(schema);
        when(this.lessonProgressRepository.save(schema)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.jpaLessonProgressDatabaseGateway.save(entity));
    }

    @Test
    @DisplayName("Find By User ID - Lesson Progress Exists")
    void findByUserIdAndLessonId_WhenLessonProgressExists_ReturnsLessonProgress() {
        // Arrange
        JpaLessonProgressSchema schema = provideLessonProgressSchemaWithId("1");
        LessonProgress mapped = provideLessonProgressWithId("1");

        when(this.lessonProgressRepository.findByUserIdAndLessonId("1", "1")).thenReturn(Optional.of(schema));
        when(this.lessonProgressSchemaMapper.fromJpaSchema(schema)).thenReturn(mapped);

        // Act
        Optional<LessonProgress> foundLessonProgress = this.jpaLessonProgressDatabaseGateway.findByUserIdAndLessonId("1", "1");

        // Assert
        assertTrue(foundLessonProgress.isPresent());
        assertEquals("1", foundLessonProgress.get().getLessonProgressId());
        verify(this.lessonProgressSchemaMapper).fromJpaSchema(schema);
    }

    @Test
    @DisplayName("Find By User ID - Lesson Progress Does Not Exist")
    void findByUserIdAndLessonId_WhenLessonProgressDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        when(this.lessonProgressRepository.findByUserIdAndLessonId("1", "1")).thenReturn(Optional.empty());

        // Act
        Optional<LessonProgress> foundLessonProgress = this.jpaLessonProgressDatabaseGateway.findByUserIdAndLessonId("1", "1");

        // Assert
        assertTrue(foundLessonProgress.isEmpty());
        verify(this.lessonProgressSchemaMapper, never()).fromJpaSchema(any());
    }

    @Test
    @DisplayName("Find All By User ID - Returns List of Lesson Progress")
    void findAllByUserId_WhenProgressExists_ReturnsList() {
        // Arrange
        JpaLessonProgressSchema schema1 = provideLessonProgressSchemaWithId("1");
        JpaLessonProgressSchema schema2 = provideLessonProgressSchemaWithId("2");
        LessonProgress mapped1 = provideLessonProgressWithId("1");
        LessonProgress mapped2 = provideLessonProgressWithId("2");

        when(this.lessonProgressRepository.findAllByUserId("1")).thenReturn(List.of(schema1, schema2));
        when(this.lessonProgressSchemaMapper.fromJpaSchema(schema1)).thenReturn(mapped1);
        when(this.lessonProgressSchemaMapper.fromJpaSchema(schema2)).thenReturn(mapped2);

        // Act
        List<LessonProgress> result = this.jpaLessonProgressDatabaseGateway.findAllByUserId("1");

        // Assert
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getLessonProgressId());
        assertEquals("2", result.get(1).getLessonProgressId());
        verify(this.lessonProgressRepository).findAllByUserId("1");
        verify(this.lessonProgressSchemaMapper).fromJpaSchema(schema1);
        verify(this.lessonProgressSchemaMapper).fromJpaSchema(schema2);
    }

    @Test
    @DisplayName("Find All By User ID - No Lesson Progress Found")
    void findAllByUserId_WhenNoProgressExists_ReturnsEmptyList() {
        // Arrange
        when(this.lessonProgressRepository.findAllByUserId("1")).thenReturn(List.of());

        // Act
        List<LessonProgress> result = this.jpaLessonProgressDatabaseGateway.findAllByUserId("1");

        // Assert
        assertTrue(result.isEmpty());
        verify(this.lessonProgressRepository).findAllByUserId("1");
        verify(this.lessonProgressSchemaMapper, never()).fromJpaSchema(any());
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

    public static JpaLessonProgressSchema provideLessonProgressSchema() {
        return new JpaLessonProgressSchema("1", "1", "1", 0, 1);
    }

    public static JpaLessonProgressSchema provideLessonProgressSchemaWithId(String id) {
        JpaLessonProgressSchema schema = provideLessonProgressSchema();
        schema.setId(id);
        return schema;
    }
}