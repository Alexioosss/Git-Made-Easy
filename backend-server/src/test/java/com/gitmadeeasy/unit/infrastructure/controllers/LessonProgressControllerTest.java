package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.controllers.LessonProgressController;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessonProgress.GetAllLessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonProgressController.class)
class LessonProgressControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private GetLessonProgress getLessonProgress;
    @MockBean private GetAllLessonProgress getAllLessonProgress;


    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exists")
    void getLessonProgress_WhenLessonProgressExists_ReturnsOk() throws Exception {
        // Arrange
        LessonProgress lessonProgress = new LessonProgress("1", "1",
                "1", "1", 0, 1);
        when(this.getLessonProgress.execute("1", "1")).thenReturn(Optional.of(lessonProgress));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/1/progress")
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(lessonProgress)));
    }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Does Not Exist")
    void getLessonProgress_WhenLessonProgressDoesNotExist_Returns404() throws Exception {
        // Arrange
        when(this.getLessonProgress.execute("1", "1")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/lessons/1/progress")
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get All Lesson Progress - Returns List")
    void getAllLessonProgress_WhenListExists_ReturnsListOfLessonProgress() throws Exception {
        // Arrange
        List<LessonProgress> progressList = List.of(
                new LessonProgress("1", "1", "1", "1", 0, 1),
                new LessonProgress("2", "1", "2", "1", 1, 3));
        when(this.getAllLessonProgress.execute("1")).thenReturn(progressList);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/progress")
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(progressList)));
    }

    @Test
    @DisplayName("Get All Lesson Progress - Empty List")
    void getAllLessonProgress_WhenEmpty_ReturnsEmptyList() throws Exception {
        // Arrange
        when(this.getAllLessonProgress.execute("1")).thenReturn(List.of());

        // Act & Assert
        this.mockMvc.perform(get("/lessons/progress")
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}