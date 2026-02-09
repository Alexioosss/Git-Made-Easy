package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.controllers.LessonProgressController;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    @MockitoBean private GetLessonProgress getLessonProgress;

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exists")
    void getLessonProgress_WhenLessonProgressExists_ReturnsOk() throws Exception {
        // Arrange
        LessonProgress lessonProgress = new LessonProgress("1", "1", "1", "1", 0, 1);
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
        when(getLessonProgress.execute("1", "1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/lessons/1/progress")
                        .with(user("1"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}