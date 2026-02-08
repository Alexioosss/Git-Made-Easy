package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonProgressController.class)
@AutoConfigureMockMvc
class LessonProgressControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    @MockitoBean private GetLessonProgress getLessonProgress;

    @BeforeEach
    void setUp() { mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build(); }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Exists")
    @WithMockUser(username = "1")
    void getLessonProgress_WhenLessonProgressExists_ReturnsOk() throws Exception {
        // Arrange
        LessonProgress lessonProgress = new LessonProgress("1", "1", "1", "1", 0, 1);
        when(this.getLessonProgress.execute("1", "1")).thenReturn(Optional.of(lessonProgress));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/1/progress"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(lessonProgress)));
    }

    @Test
    @DisplayName("Get Lesson Progress - Lesson Progress Does Not Exist")
    @WithMockUser(username = "1")
    void getLessonProgress_WhenLessonProgressDoesNotExist_Returns404() throws Exception {
        // Arrange
        when(getLessonProgress.execute("1", "1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/lessons/1/progress"))
                .andExpect(status().isNotFound());
    }
}