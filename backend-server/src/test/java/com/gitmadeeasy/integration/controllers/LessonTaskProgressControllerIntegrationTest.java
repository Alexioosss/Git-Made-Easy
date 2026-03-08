package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa.JpaTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskAttemptRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LessonTaskProgressControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaTaskAttemptRepository taskAttemptRepository;
    @Autowired private JpaTaskRepository taskRepository;
    @Autowired private JpaLessonRepository lessonRepository;

    private static final String USER_ID = "1";
    private String lessonId;

    @BeforeEach
    void setUp() {
        this.taskAttemptRepository.deleteAll();
        this.taskRepository.deleteAll();
        this.lessonRepository.deleteAll();
        lessonId = createLessonAndReturnLessonId();
    }


    @Test
    @DisplayName("Record Task Attempt - Valid Request")
    void recordTaskAttempt_WhenValidRequest_ReturnsTaskProgress() throws Exception {
        // Arrange
        String taskId = createTaskAndReturnTaskId();
        TaskAttemptRequest request = new TaskAttemptRequest("git init");

        // Act & Assert
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.attempts").value(1))
                .andExpect(jsonPath("$.lastInput").value("git init"))
                .andExpect(jsonPath("$.lastError").isEmpty())
                .andExpect(jsonPath("$.startedAt").exists());
    }

    @Test
    @DisplayName("Record Task Attempt - Missing Required Fields")
    void recordTaskAttempt_WhenMissingRequiredFields_ReturnsBadRequest() throws Exception {
        // Arrange
        String taskId = createTaskAndReturnTaskId();
        TaskAttemptRequest invalid = new TaskAttemptRequest("");

        // Act & Assert
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalid))
                        .with(user(USER_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Task Attempt - Progress Exists")
    void getTaskAttempt_WhenTaskAttemptExists_ReturnsTaskProgress() throws Exception {
        // Arrange
        String taskId = createTaskAndReturnTaskId();
        TaskAttemptRequest request = new TaskAttemptRequest("git init");

        // Act & Assert
        // Save a task attempt
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user(USER_ID)))
                .andExpect(status().isOk());

        // To then retrieve it
        this.mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .with(user(USER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.attempts").value(1))
                .andExpect(jsonPath("$.lastInput").value("git init"))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("Get Task Attempt - Progress Does Not Exist")
    void getTaskAttempt_WhenTaskAttemptDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        String taskId = createTaskAndReturnTaskId();

        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .with(user(USER_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Task Attempt - Progress Belongs to Another User")
    void getTaskAttempt_WhenTaskAttemptBelongsToAnotherUser_ReturnsNotFound() throws Exception {
        // Arrange
        String taskId = createTaskAndReturnTaskId();
        TaskAttemptRequest request = new TaskAttemptRequest("git init");

        // Act & Assert
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request))
                        .with(user("2")))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}/progress", lessonId, taskId)
                        .with(user(USER_ID)))
                .andExpect(status().isNotFound());
    }



    // ----- HELPER METHODS ----- //


    private String createTaskAndReturnTaskId() {
        JpaTaskSchema taskSchema = new JpaTaskSchema(
                lessonId, "Intro to Git", "Creating a new Git repository",
                "git init", "Remember to initialise the new repository",
                1, DifficultyLevels.EASY);
        return this.taskRepository.save(taskSchema).getId();
    }

    private String createLessonAndReturnLessonId() {
        JpaLessonSchema schema = new JpaLessonSchema("Intro to Git", "Learn Git basics", DifficultyLevels.EASY, 1);
        return this.lessonRepository.save(schema).getId();
    }
}