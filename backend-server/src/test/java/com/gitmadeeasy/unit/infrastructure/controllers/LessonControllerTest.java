package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.infrastructure.controllers.LessonController;
import com.gitmadeeasy.infrastructure.dto.lessons.LessonResponse;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetAllLessons;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.GetNextLesson;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private CreateLesson createLesson;
    @MockBean private GetLessonById getLessonById;
    @MockBean private GetAllLessons getAllLessons;
    @MockBean private GetNextLesson getNextLesson;


    @Test
    @DisplayName("Create Lesson - Valid Payload Returns Successful Response / 200")
    void createLesson_WhenValidPayload_ReturnsLesson() throws Exception {
        // Arrange
        CreateLessonRequest validRequest = new CreateLessonRequest(
                "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                "easy", 1, null, null);
        Lesson createdLesson = new Lesson("1", "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                DifficultyLevels.EASY, 1);
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
        CreateLessonRequest invalidRequest = new CreateLessonRequest(
                "", "", "", 0, null, null);

        // Act & Assert
        this.mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get All Lessons - Returns Successful Response / 200")
    void getAllLessons_WhenLessonsExist_ReturnsListOfLessons() throws Exception {
        // Arrange
        Lesson lesson1 = new Lesson("1", "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                DifficultyLevels.EASY, 1, "", List.of());
        Lesson lesson2 = new Lesson("2", "Branching",
                "Learn how to branch in Git.", DifficultyLevels.MEDIUM, 2, "", List.of());
        List<Lesson> lessons = List.of(lesson1, lesson2);
        when(this.getAllLessons.execute()).thenReturn(lessons);
        LessonResponse lesson1ResponseDTO = new LessonResponse(lesson1);
        LessonResponse lesson2ResponseDTO = new LessonResponse(lesson2);

        // Act & Assert
        this.mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(List.of(lesson1ResponseDTO, lesson2ResponseDTO))));
    }

    @Test @DisplayName("Get All Lessons - Empty Lessons List Returns 200 With Empty List")
    void getAllLessons_WhenEmptyLessonsList_ReturnsEmptyList() throws Exception {
        // Arrange
        when(this.getAllLessons.execute()).thenReturn(List.of());

        // Act & Assert
        this.mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Get Lesson By ID - Lesson Exists - Returns Successful Response / 200")
    void getLessonById_WhenLessonExists_ReturnsLesson() throws Exception {
        // Arrange
        String lessonId = "1";
        Lesson foundLesson = new Lesson(lessonId,
                "Intro to Git",
                "A simple introduction to an industry-standard version control system.",
                DifficultyLevels.EASY, 1);
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

    @Test
    @DisplayName("Get Next Lesson - Next Lesson Exists - Returns Successful Response / 200")
    void getNextLesson_WhenNextLessonExistsForCurrentLesson_ReturnsNextLesson() throws Exception {
        // Arrange
        String lessonId = "1";
        Lesson nextLesson = new Lesson("2", "Intro 2", "Description", DifficultyLevels.EASY, 2);
        when(this.getNextLesson.execute(lessonId)).thenReturn(nextLesson);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/next"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId").value("2"))
                .andExpect(jsonPath("$.lessonOrder").value("2"));
    }

    @Test
    @DisplayName("Get Next Lesson - Next Lesson Does Not Exist - Returns Null")
    void getNextLesson_WhenNextLessonExists_ReturnsNull() throws Exception {
        // Arrange
        String lessonId = "1";
        when(this.getNextLesson.execute(lessonId)).thenReturn(null);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/next"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Get Next Lesson - Current Lesson Does Not Exist - Returns Unsuccessful Response / 404")
    void getNextLesson_WhenCurrentLessonDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        String lessonId = "1";
        when(this.getNextLesson.execute(lessonId)).thenThrow(new LessonNotFoundWithIdException(lessonId));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/next"))
                .andExpect(status().isNotFound());
    }
}