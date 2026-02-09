package com.gitmadeeasy.unit.infrastructure.mappers.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;
import com.gitmadeeasy.infrastructure.mappers.lessonProgress.LessonProgressSchemaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LessonProgressSchemaMapperTest {
    private static final LessonProgressSchemaMapper mapper = new LessonProgressSchemaMapper();

    @Test
    @DisplayName("To Schema - Maps LessonProgress Domain Entity to Persistence Schema")
    void toSchema_WhenGivenDomainLessonProgress_MapsToPersistenceSchema() {
        // Arrange
        LessonProgress progress = new LessonProgress("1", "1", "1", "1", 1, 3);

        // Act
        LessonProgressSchema schema = mapper.toSchema(progress);

        // Assert
        assertEquals("1", schema.getUserId());
        assertEquals("1", schema.getLessonId());
        assertEquals("1", schema.getCurrentTaskProgressId());
        assertEquals(1, schema.getCompletedTasksCount());
        assertEquals(3, schema.getTotalTasksCount());
    }

    @Test
    @DisplayName("To Entity - Maps Persistence Schema to LessonProgress Domain Entity")
    void toEntity_WhenGivenPersistenceSchema_MapsToDomainLessonProgress() {
        // Arrange
        LessonProgressSchema schema = new LessonProgressSchema( "1", "1", "1", 1, 3);
        schema.setLessonProgressId(Long.valueOf("1"));

        // Act
        LessonProgress progress = mapper.toEntity(schema);

        // Assert
        assertEquals("1", progress.getLessonProgressId());
        assertEquals("1", progress.getUserId());
        assertEquals("1", progress.getLessonId());
        assertEquals("1", progress.getCurrentTaskProgressId());
        assertEquals(1, progress.getCompletedTasksCount());
        assertEquals(3, progress.getTotalTasksCount());
    }
}