package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.configs.RepositoriesConfiguration;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(RepositoriesConfiguration.class)
class TaskProgressControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private TaskAttemptRepository taskAttemptRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private LessonRepository lessonRepository;

    private static final String USER_ID = "1";
    private String lessonId;

    @BeforeEach
    void setUp() {
        this.taskAttemptRepository.deleteAll();
        this.taskRepository.deleteAll();
        this.lessonRepository.deleteAll();
        lessonId = createLesson();
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
        TaskSchema taskSchema = new TaskSchema(
                lessonId, "Intro to Git", "Creating a new Git repository",
                "git init", "Remember to INITialise the new repository", 1);
        return this.taskRepository.save(taskSchema).getId();
    }

    private String createLesson() {
        LessonSchema schema = new LessonSchema("Intro to Git", "Learn Git basics", LessonDifficulty.EASY);
        return this.lessonRepository.save(schema).getId();
    }
}