package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.dashboard.DashboardResponse;
import com.gitmadeeasy.usecases.dashboard.GetDashboardData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardIntegrationTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean private GetDashboardData getDashboardData;


    @Test
    @WithMockUser(username = "user1")
    @DisplayName("GET /dashboard - Authenticated user receives their personalised dashboard data")
    void getDashboard_WhenAuthenticated_ReturnsDashboardData() throws Exception {
        // Arrange
        DashboardResponse response = new DashboardResponse(
                "user1", "John", "Doe", List.of(), List.of());
        when(this.getDashboardData.execute("user1")).thenReturn(response);

        // Act & Assert
        this.mockMvc.perform(get("/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(response)));
    }

    @Test
    @DisplayName("GET /dashboard – Unauthenticated User - Returns 401")
    void getDashboard_WhenUnauthenticated_ReturnsUnauthorized() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}