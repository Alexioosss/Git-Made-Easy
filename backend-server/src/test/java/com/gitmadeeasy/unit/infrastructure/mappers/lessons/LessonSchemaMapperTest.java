package com.gitmadeeasy.unit.infrastructure.mappers.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LessonSchemaMapperTest {
    private static final LessonSchemaMapper mapper = new LessonSchemaMapper();

    @Test
    @DisplayName("To Schema - Maps Lesson Domain Entity to Persistence Schema")
    void toSchema_WhenGivenDomainLesson_MapsToPersistenceSchema() {
        // Arrange
        Lesson lesson = new Lesson( "Intro to Git", "Learn the basics of Git", LessonDifficulty.EASY );

        // Act
        LessonSchema schema = mapper.toSchema(lesson);

        // Assert
        assertEquals("Intro to Git", schema.getTitle());
        assertEquals("Learn the basics of Git", schema.getDescription());
        assertEquals(LessonDifficulty.EASY, schema.getDifficulty());
    }

    @Test
    @DisplayName("To Entity - Maps Persistence Schema to Lesson Domain Entity")
    void toEntity_WhenGivenPersistenceSchema_MapsToDomainLesson() {
        // Arrange
        LessonSchema schema = new LessonSchema( "Intro to Git", "Learn the basics of Git", LessonDifficulty.EASY );

        // Act
        Lesson lesson = mapper.toEntity(schema);

        // Assert
        assertEquals("Intro to Git", lesson.getTitle());
        assertEquals("Learn the basics of Git", lesson.getDescription());
        assertEquals(LessonDifficulty.EASY, lesson.getDifficulty());
    }
}