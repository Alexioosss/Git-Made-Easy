package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.infrastructure.controllers.DashboardController;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.dashboard.DashboardResponse;
import com.gitmadeeasy.usecases.dashboard.GetDashboardData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean private GetDashboardData getDashboardData;


    @Test
    @DisplayName("GET /dashboard - Valid Principal Returns Dashboard Data")
    void getDashboard_WhenPrincipalIsValid_ReturnsDashboardData() throws Exception {
        // Arrange
        Principal principal = () -> "user1";
        DashboardResponse dashboardResponse = new DashboardResponse
                ("user1", "John", "Doe", List.of(), List.of());
        when(this.getDashboardData.execute("user1")).thenReturn(dashboardResponse);

        // Act & Assert
        this.mockMvc.perform(get("/dashboard")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(dashboardResponse)));
    }

    @Test
    @DisplayName("GET /dashboard – Missing principal returns 500")
    void getDashboard_WhenPrincipalMissing_ReturnsServerError() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}