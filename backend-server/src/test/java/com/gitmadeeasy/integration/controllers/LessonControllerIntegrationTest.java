package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private LessonRepository lessonRepository;

    @BeforeEach
    void setUp() { this.lessonRepository.deleteAll(); }


    @Test
    @DisplayName("Create Lesson - Valid Request")
    void createLesson_WhenValidRequestData_ReturnsCreatedLesson() throws Exception {
        // Arrange
        CreateLessonRequest request = new CreateLessonRequest(
                "Intro to Git", "An introduction to an industry-standard technology",
                "easy");

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lessonId").exists())
                .andExpect(jsonPath("title").value("Intro to Git"))
                .andExpect(jsonPath("description").value("An introduction to an industry-standard technology"))
                .andExpect(jsonPath("difficulty").value(LessonDifficulty.EASY.name()))
                .andExpect(jsonPath("tasks").isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideInvalidLessonCreationRequest")
    @DisplayName("Create Lesson - Invalid Request")
    void createLesson_WhenInvalidRequestData_ReturnsBadRequest(String displayName, CreateLessonRequest invalidRequest) throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists")
    void getLessonById_WhenLessonExists_ReturnsLesson() throws Exception {
        // Arrange
        String lessonId = createLessonAndReturnLessonId();

        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}", lessonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value(lessonId))
                .andExpect(jsonPath("$.title").value("Intro to Git"))
                .andExpect(jsonPath("$.description").value("An introduction to an industry-standard technology"))
                .andExpect(jsonPath("$.difficulty").value(LessonDifficulty.EASY.name()))
                .andExpect(jsonPath("$.tasks").isEmpty());
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Does Not Exist")
    void getLessonById_WhenLessonDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}", "0"))
                .andExpect(status().isNotFound());
    }



    // ----- HELPER METHODS ----- //


    private String createLessonAndReturnLessonId() {
        LessonSchema lessonSchema = new LessonSchema(
                "Intro to Git",
                "An introduction to an industry-standard technology",
                LessonDifficulty.EASY);
        return this.lessonRepository.save(lessonSchema).getId();
    }

    private static Stream<Arguments> provideInvalidLessonCreationRequest() {
        return Stream.of(
                Arguments.of("Missing Title",
                        new CreateLessonRequest("", "An introduction to an industry-standard technology", "easy")),
                Arguments.of("Missing Description",
                        new CreateLessonRequest("Intro to Git", "", "easy")),
                Arguments.of("Missing Difficulty",
                        new CreateLessonRequest("Intro to Git", "An introduction to an industry-standard technology", ""))
        );
    }
}