package com.gitmadeeasy.unit.infrastructure.mappers.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskSchemaMapperTest {
    private static final TaskSchemaMapper mapper = new TaskSchemaMapper();

    @Test
    @DisplayName("To Schema - Maps Task Domain Entity To Persistence Schema")
    void toSchema_WhenGivenDomainTask_MapsToPersistenceSchema() {
        // Arrange
        Task task = new Task( "1", "Intro to Git", "An introduction to an industry standard technology",
                "start", "As simple as it seems...", 1 );

        // Act
        TaskSchema schema = mapper.toSchema(task);

        // Assert
        assertEquals("1", schema.getLessonId());
        assertEquals("Intro to Git", schema.getTitle());
        assertEquals("An introduction to an industry standard technology", schema.getContent());
        assertEquals("start", schema.getExpectedCommand());
        assertEquals("As simple as it seems...", schema.getHint());
        assertEquals(1, schema.getTaskOrder());
    }

    @Test
    @DisplayName("To Schema - Maps Persistence Schema To Task Domain Entity")
    void toEntity_henGivenPersistenceSchema_MapsToDomainTask() {
        // Arrange
        TaskSchema schema = new TaskSchema("1", "Intro to Git", "An introduction to an industry standard technology",
                "start", "As simple as it seems...", 1 );

        // Act
        Task task = mapper.toEntity(schema);

        // Assert
        assertEquals("1", task.getLessonId());
        assertEquals("Intro to Git", task.getTitle());
        assertEquals("An introduction to an industry standard technology", task.getContent());
        assertEquals("start", task.getExpectedCommand());
        assertEquals("As simple as it seems...", task.getHint());
        assertEquals(1, task.getTaskOrder());
    }
}