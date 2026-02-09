package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.controllers.LessonController;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockitoBean private CreateLesson createLesson;
    @MockitoBean private GetLessonById getLessonById;


    @Test
    @DisplayName("Create Lesson - Valid Payload Returns Successful Response / 200")
    void createLesson_WhenValidPayload_ReturnsLesson() throws Exception {
        // Arrange
        CreateLessonRequest validRequest = new CreateLessonRequest(
                "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                "easy"
        );
        Lesson createdLesson = new Lesson("1", "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                LessonDifficulty.EASY);
        when(this.createLesson.execute(validRequest)).thenReturn(createdLesson);

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.objectToJson(createdLesson)));
    }

    @Test
    @DisplayName("Create Lesson - Invalid Payload Returns Unsuccessful Response / 400")
    void createLesson_WhenInvalidPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        CreateLessonRequest invalidRequest = new CreateLessonRequest("", "", "");

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists - Returns Successful Response / 200")
    void getLessonById_WhenLessonExists_ReturnsLesson() throws Exception {
        // Arrange
        String lessonId = "1";
        Lesson foundLesson = new Lesson(lessonId,
                "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                LessonDifficulty.EASY);
        when(this.getLessonById.execute("1")).thenReturn(foundLesson);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(foundLesson)));
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Does Not Exist - Returns Unsuccessful Response / 404")
    void getLessonById_WhenLessonDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        String lessonId = "1";
        when(this.getLessonById.execute(lessonId)).thenThrow(new LessonNotFoundWithIdException(lessonId));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId))
                .andExpect(status().isNotFound());
    }
}