package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.dashboard.DashboardResponse;
import com.gitmadeeasy.usecases.dashboard.GetDashboardData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final GetDashboardData getDashboardData;
    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    public DashboardController(GetDashboardData getDashboardData) {
        this.getDashboardData = getDashboardData;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(Principal principal) {
        log.info("GET /dashboard - Fetching dashboard data for user {}", principal.getName());
        DashboardResponse response = this.getDashboardData.execute(principal.getName());
        return ResponseEntity.ok(response);
    }
}