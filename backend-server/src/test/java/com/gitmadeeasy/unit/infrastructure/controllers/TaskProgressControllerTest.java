package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.controllers.TaskProgressController;
import com.gitmadeeasy.infrastructure.gateways.security.UserPrincipal;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.attemptTask.AttemptTask;
import com.gitmadeeasy.usecases.attemptTask.GetTaskProgress;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotInLessonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskProgressController.class)
class TaskProgressControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockitoBean private AttemptTask attemptTask;
    @MockitoBean private GetTaskProgress getTaskProgress;

    private static final String USER_ID = "1";
    private static final String LESSON_ID = "1";
    private static final String TASK_ID = "1";

    @Test
    @DisplayName("Record Task Attempt - Valid Payload - Returns 200")
    void recordTaskAttempt_WhenValidPayload_ReturnsTaskProgress() throws Exception {
        // Arrange
        TaskAttemptRequest request = new TaskAttemptRequest("input");
        TaskProgress progress = new TaskProgress(null, USER_ID, TASK_ID);
        progress.recordAttempt("input");
        progress.markCompleted();
        when(this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request)).thenReturn(progress);

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(USER_ID)) // Simulate setting the User ID in the AuthenticationPrincipal
                        .with(csrf())) // Spring requires a CSRF token on POST requests, this simulates a valid CSRF token
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(progress)));
    }

    @Test
    @DisplayName("Record Task Attempt - Invalid Payload - Returns 400")
    void recordTaskAttempt_WhenInvalidPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        TaskAttemptRequest request = new TaskAttemptRequest("");
        when(this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request))
                .thenThrow(new IllegalArgumentException("Invalid request"));

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(new UserPrincipal(USER_ID)))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Record Task Attempt - Lesson Not Found - Returns 404")
    void recordTaskAttempt_WhenLessonNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        TaskAttemptRequest request = new TaskAttemptRequest("git init");
        when(this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request))
                .thenThrow(new LessonNotFoundWithIdException(LESSON_ID));

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(new UserPrincipal(USER_ID)))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Record Task Attempt - Task Not Found - Returns 404")
    void recordTaskAttempt_WhenTaskNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        TaskAttemptRequest request = new TaskAttemptRequest("git init");
        when(this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request))
                .thenThrow(new TaskNotFoundWithIdException(LESSON_ID, TASK_ID));

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(new UserPrincipal(USER_ID)))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Record Task Attempt - Task Not In Lesson - Returns 400")
    void recordTaskAttempt_WhenTaskNotInLesson_ReturnsBadRequest() throws Exception {
        // Arrange
        TaskAttemptRequest request = new TaskAttemptRequest("git init");
        when(this.attemptTask.attempt(USER_ID, LESSON_ID, TASK_ID, request))
                .thenThrow(new TaskNotInLessonException(LESSON_ID, TASK_ID));

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(new UserPrincipal(USER_ID)))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Task Attempt - Task Progress Exists - Returns 200")
    void getTaskAttempt_WhenExists_ReturnsOk() throws Exception {
        // Arrange
        TaskProgress progress = new TaskProgress(null, USER_ID, TASK_ID);
        progress.markCompleted();
        when(this.getTaskProgress.execute(USER_ID, LESSON_ID, TASK_ID)).thenReturn(Optional.of(progress));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                            .with(user(new UserPrincipal(USER_ID)))
                            .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(progress)));
    }

    @Test
    @DisplayName("Get Task Progress - Task Progress Not Found - Returns 404")
    void getTaskAttempt_WhenNotFound_Returns404() throws Exception {
        // Arrange
        when(this.getTaskProgress.execute(USER_ID, LESSON_ID, TASK_ID)).thenReturn(Optional.empty());

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + LESSON_ID + "/tasks/" + TASK_ID + "/progress")
                        .with(user(new UserPrincipal(USER_ID)))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}