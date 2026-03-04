package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
import com.gitmadeeasy.testConfig.TestConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LessonControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaLessonRepository lessonRepository;

    @BeforeEach
    void setUp() { this.lessonRepository.deleteAll(); }


    @Test
    @DisplayName("Create Lesson - Valid Request")
    void createLesson_WhenValidRequestData_ReturnsCreatedLesson() throws Exception {
        // Arrange
        CreateLessonRequest request = new CreateLessonRequest(
                "Intro to Git", "An introduction to an industry-standard technology",
                "easy", 1, null, null);

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lessonId").exists())
                .andExpect(jsonPath("title").value("Intro to Git"))
                .andExpect(jsonPath("description").value("An introduction to an industry-standard technology"))
                .andExpect(jsonPath("difficulty").value(DifficultyLevels.EASY.name()))
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
    @DisplayName("Create Lesson - Invalid Request - Unrecognised Lesson Difficulty")
    void createLesson_WhenInvalidRequestData_ReturnsBadRequest() throws Exception {
        // Arrange
        CreateLessonRequest invalidRequest = new CreateLessonRequest("Intro to Git",
                "An introduction to an industry-standard technology", "impossible", 1, null, null);

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get All Lessons - No Lessons Exist - Returns Empty List")
    void getAllLessons_WhenNoLessonsExist_ReturnsEmptyList() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Get All Lessons - Lessons Exist - Returns Lessons List")
    void getAllLessons_WhenLessonsExist_ReturnsLessonsList() throws Exception {
        // Arrange
        JpaLessonSchema lesson1 = new JpaLessonSchema( "Intro to Git",
                "An introduction to an industry-standard technology", DifficultyLevels.EASY, 1);
        JpaLessonSchema lesson2 = new JpaLessonSchema( "Branching", "Learn how to branch in Git",
                DifficultyLevels.MEDIUM, 2);
        this.lessonRepository.save(lesson1);
        this.lessonRepository.save(lesson2);

        // Act & Assert
        this.mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Intro to Git"))
                .andExpect(jsonPath("$[1].title").value("Branching"));
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
                .andExpect(jsonPath("$.difficulty").value(DifficultyLevels.EASY.name()))
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
        JpaLessonSchema lessonSchema = new JpaLessonSchema(
                "Intro to Git",
                "An introduction to an industry-standard technology",
                DifficultyLevels.EASY, 1);
        return this.lessonRepository.save(lessonSchema).getId();
    }

    private static Stream<Arguments> provideInvalidLessonCreationRequest() {
        return Stream.of(
                Arguments.of("Missing Title",
                        new CreateLessonRequest("", "An introduction to an industry-standard technology",
                                "easy", 1, null, null)),
                Arguments.of("Missing Description",
                        new CreateLessonRequest("Intro to Git", "", "easy", 1, null, null)),
                Arguments.of("Missing Difficulty",
                        new CreateLessonRequest("Intro to Git", "An introduction to an industry-standard technology",
                                "", 1, null, null))
        );
    }
}