package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.controllers.TaskProgressController;
import com.gitmadeeasy.infrastructure.gateways.security.UserPrincipal;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.taskProgress.GetAllTaskProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.gitmadeeasy.testUtil.TaskTestDataFactory.taskProgressesList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskProgressController.class)
@AutoConfigureMockMvc
public class TaskProgressControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private GetAllTaskProgress getAllTaskProgress;

    @Test
    @DisplayName("Get All Task Progress - Task Progresses Found For User")
    void getAllTaskProgress_WhenTaskProgressesFound_ReturnsTaskProgressList() throws Exception {
        // Arrange
        List<TaskProgress> taskProgresses = taskProgressesList();
        when(this.getAllTaskProgress.execute("1")).thenReturn(taskProgresses);

        // Act & Assert
        this.mockMvc.perform(get("/tasks/progress")
                        .with(user(new UserPrincipal("1"))))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(taskProgresses)));
    }

    @Test
    @DisplayName("Get All Task Progress - Task Progresses Not Found For User")
    void getAllTaskProgress_WhenNoTaskProgressesFound_ReturnsEmptyTaskProgressList() throws Exception {
        // Arrange
        when(this.getAllTaskProgress.execute("1")).thenReturn(List.of());

        // Act & Assert
        this.mockMvc.perform(get("/tasks/progress")
                .with(user(new UserPrincipal("1"))))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}