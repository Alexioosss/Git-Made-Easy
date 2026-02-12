package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
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
class TaskControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private LessonRepository lessonRepository;
    @Autowired private TaskRepository taskRepository;
    private String lessonId;

    @BeforeEach
    void setUp() {
        this.taskRepository.deleteAll();
        this.lessonRepository.deleteAll();
        lessonId = createLessonAndReturnLessonId();
    }

    @Test
    @DisplayName("Create Task - Valid Request")
    void createTask_WhenValidRequestData_ReturnsCreatedTask() throws Exception {
        // Arrange
        CreateTaskRequest request = new CreateTaskRequest(
                "Intro to Git", "Initialise a repository",
                "git init", "Remember to INITialize the new repository", 1);

        // Act & Assert
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").exists())
                .andExpect(jsonPath("$.title").value("Intro to Git"))
                .andExpect(jsonPath("$.content").value("Initialise a repository"))
                .andExpect(jsonPath("$.expectedCommand").value("git init"))
                .andExpect(jsonPath("$.hint").value("Remember to INITialize the new repository"))
                .andExpect(jsonPath("$.taskOrder").value(1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideInvalidTaskCreationRequests")
    @DisplayName("Create Task - Missing Required Fields")
    void createTask_WhenMissingRequiredFields_ReturnsBadRequest(String displayName, CreateTaskRequest invalidRequest) throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/lessons/{lessonId}/tasks", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Task By ID - Task Exists")
    void getTaskById_WhenTaskExists_ReturnsTask() throws Exception {
        String taskId = createTaskAndReturnTaskId();

        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}", lessonId, taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.title").value("Initialise a Git repository"))
                .andExpect(jsonPath("$.content").value("Create a new Git repository"))
                .andExpect(jsonPath("$.expectedCommand").value("git init"))
                .andExpect(jsonPath("$.hint").value("Remember to INITialise the new repository"))
                .andExpect(jsonPath("$.taskOrder").value(1));
    }

    @Test
    @DisplayName("Get Task By ID - Task Does Not Exist")
    void getTaskById_WhenTaskDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}", lessonId, "0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Task By ID - Lesson Does Not Exist")
        // Act & Assert
    void getTaskById_WhenLessonDoesNotExist_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/lessons/{lessonId}/tasks/{taskId}", "0", "0"))
                .andExpect(status().isNotFound());
    }



    // ----- HELPER METHODS ----- //


    private String createLessonAndReturnLessonId() {
        LessonSchema lessonSchema = new LessonSchema(
                "Into to Git",
                "Learn the basics of an industry-standard technology",
                LessonDifficulty.EASY);
        return this.lessonRepository.save(lessonSchema).getId();
    }

    private String createTaskAndReturnTaskId() {
        TaskSchema taskSchema = new TaskSchema(
                lessonId, "Initialise a Git repository",
                "Create a new Git repository",
                "git init",
                "Remember to INITialise the new repository", 1);
        return this.taskRepository.save(taskSchema).getId();
    }

    private static Stream<Arguments> provideInvalidTaskCreationRequests() {
        return Stream.of(
                Arguments.of("Missing Title",
                        new CreateTaskRequest("", "content", "git init", null, 1)),
                Arguments.of("Missing Content",
                        new CreateTaskRequest("Title", "", "git init", null, 1)),
                Arguments.of("Missing Expected Command",
                        new CreateTaskRequest("Title", "content", "", null, 1))
        );
    }
}