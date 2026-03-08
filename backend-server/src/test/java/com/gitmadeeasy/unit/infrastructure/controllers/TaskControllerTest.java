package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.controllers.TaskController;
import com.gitmadeeasy.infrastructure.dto.tasks.TaskResponse;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskResponseMapper;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.testUtil.TaskTestDataFactory;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.GetTasksForLesson;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TaskResponseMapper.class)
class TaskControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private TaskResponseMapper mapper;
    @MockitoBean private CreateTask createTask;
    @MockitoBean private GetTaskById getTaskById;
    @MockitoBean private GetTasksForLesson getTasksForLesson;


    @Test
    @DisplayName("Create Task - Valid Payload Returns Successful Response / 201")
    void createTask_WhenValidPayload_ReturnsTask() throws Exception {
        // Arrange
        String lessonId = "1";
        CreateTaskRequest validRequest = new CreateTaskRequest(
                "first git task", "Let's get started, shall we", "git start",
                "easier than it may seem...", 1, "easy");
        Task createdTask = TaskTestDataFactory.task();
        when(this.createTask.execute(lessonId, validRequest)).thenReturn(createdTask);
        TaskResponse expectedResponse = this.mapper.toResponse(createdTask);

        // Act & Assert
        this.mockMvc.perform(post("/lessons/" + lessonId + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.objectToJson(expectedResponse)));
    }

    @Test
    @DisplayName("Create Task - Invalid Payload Returns Unsuccessful Response / 400")
    void createTask_WhenInvalidPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        CreateTaskRequest invalidRequest = new CreateTaskRequest(
                "", "", "", "", 0, "easy");
        when(this.createTask.execute("0", invalidRequest))
                .thenThrow(new MissingRequiredFieldException("fields cannot be left blank"));

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
        String lessonId = "1";
        String taskId = "1";
        Task foundTask = TaskTestDataFactory.task();
        when(this.getTaskById.execute(lessonId, taskId)).thenReturn(foundTask);
        TaskResponse expectedResponse = this.mapper.toResponse(foundTask);

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(expectedResponse)));
    }

    @Test
    @DisplayName("Get Task By ID - Task Does Not Exist - Returns Unsuccessful Response / 404")
    void getTaskById_WhenTaskDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        String lessonId = "1";
        String taskId = "1";
        when(this.getTaskById.execute(lessonId, taskId)).thenThrow(new TaskNotFoundWithIdException(lessonId, taskId));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Tasks For Lesson - Tasks Exist - Returns Successful Response / 200")
    void getTasksForLesson_WhenTasksExist_ReturnsList() throws Exception {
        // Arrange
        String lessonId = "1";
        Task t1 = TaskTestDataFactory.task();
        Task t2 = TaskTestDataFactory.task();
        when(this.getTasksForLesson.execute(lessonId)).thenReturn(List.of(t1, t2));

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(List.of(t1, t2))));
    }

    @Test
    @DisplayName("Get Tasks For Lesson - No Tasks Exist - Returns Empty List / 200")
    void getTasksForLesson_WhenNoTasksExist_ReturnsEmptyList() throws Exception {
        // Arrange
        String lessonId = "1";
        when(this.getTasksForLesson.execute(lessonId)).thenReturn(List.of());

        // Act & Assert
        this.mockMvc.perform(get("/lessons/" + lessonId + "/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}