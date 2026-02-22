package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressSchema;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa.JpaLessonProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LessonProgressControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaLessonProgressRepository lessonProgressRepository;
    private static final String LESSON_ID = "1";
    private static final String USER_ID = "1";

    @BeforeEach
    void setUp() { this.lessonProgressRepository.deleteAll(); }


    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exists")
    void getLessonProgress_WhenLessonProgressExists_ReturnsLessonProgress() throws Exception {
        // Arrange
        JpaLessonProgressSchema schema = new JpaLessonProgressSchema(
                USER_ID, LESSON_ID, "1", 1, 3);
        JpaLessonProgressSchema savedSchema = this.lessonProgressRepository.save(schema);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/progress", LESSON_ID)
                        .with(user(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonProgressId").value(savedSchema.getId()))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.lessonId").value(LESSON_ID))
                .andExpect(jsonPath("$.currentTaskProgressId").value(1))
                .andExpect(jsonPath("$.completedTasksCount").value(1))
                .andExpect(jsonPath("$.totalTasksCount").value(3));
    }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Does Not Exist")
    void getLessonProgress_WhenLessonProgressDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/progress", LESSON_ID)
                        .with(user(USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exist But Belongs To Another User")
    void getLessonProgress_WhenLessonProgressBelongsToAnotherUser_ReturnsNotFound() throws Exception {
        // Arrange
        JpaLessonProgressSchema schema = new JpaLessonProgressSchema(
                "2", LESSON_ID, "1", 1, 3);
        this.lessonProgressRepository.save(schema);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/progress", LESSON_ID)
                        .with(user(USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get All Lesson Progress - Returns List")
    void getAllLessonProgress_WhenProgressExists_ReturnsList() throws Exception {
        // Arrange
        JpaLessonProgressSchema schema1 = new JpaLessonProgressSchema(
                USER_ID, "1", "1", 1, 3);
        JpaLessonProgressSchema schema2 = new JpaLessonProgressSchema(
                USER_ID, "2", "2", 2, 5);
        this.lessonProgressRepository.save(schema1);
        this.lessonProgressRepository.save(schema2);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/progress")
                        .with(user(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(USER_ID))
                .andExpect(jsonPath("$[1].userId").value(USER_ID));
    }

    @Test
    @DisplayName("Get All Lesson Progress - No Progress Exists")
    void getAllLessonProgress_WhenNoProgressExists_ReturnsEmptyList() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/lessons/progress")
                        .with(user(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}