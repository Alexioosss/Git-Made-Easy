package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.controllers.TaskController;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import com.gitmadeeasy.usecases.validation.MissingRequiredFieldException;
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

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockitoBean private CreateTask createTask;
    @MockitoBean private GetTaskById getTaskById;



    @Test
    @DisplayName("Create Task - Valid Payload Returns Successful Response / 200")
    void createTask_WhenValidPayload_ReturnsTask() throws Exception {
        // Arrange
        String lessonId = "1";
        CreateTaskRequest validRequest = new CreateTaskRequest(
                "first git task", "Let's get started, shall we",
                "git start", "easier than it may seem...", 1
        );
        Task createdTask = new Task(
                "1", lessonId,
                "first git task", "Let's get started, shall we",
                "git start", "easier than it may seem...", 1
        );
        when(this.createTask.execute(lessonId, validRequest)).thenReturn(createdTask);

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + lessonId + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.objectToJson(createdTask)));
    }

    @Test
    @DisplayName("Create Task - Invalid Payload Returns Unsuccessful Response / 400")
    void createTask_WhenInvalidPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        CreateTaskRequest invalidRequest = new CreateTaskRequest("", "", "", "", 0);
        when(this.createTask.execute("0", invalidRequest)).thenThrow(new MissingRequiredFieldException("fields cannot be left blank"));

        // Act & Assert
        this.mockMvc.perform(post("/lessons/0/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get Task By ID - Task Exists - Returns Successful Response / 200")
    void getTaskById_WhenTaskExists_ReturnsTask() throws Exception {
        // Arrange
        String lessonId = "1", taskId = "1";
        Task foundTask = new Task(
                "1", "first git task",
                "Let's get started, shall we",
                "git start", "easier than it may seem...", 1
        );
        when(this.getTaskById.execute(lessonId, taskId)).thenReturn(foundTask);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(foundTask)));
    }

    @Test
    @DisplayName("Get Task By ID - Task Does Not Exist - Returns Unsuccessful Response / 404")
    void getTaskById_WhenTaskDoesNotExist_ReturnsNotFound() throws Exception {
        String lessonId = "1", taskId = "1";
        when(this.getTaskById.execute(lessonId, taskId)).thenThrow(new TaskNotFoundWithIdException(lessonId, taskId));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }
}