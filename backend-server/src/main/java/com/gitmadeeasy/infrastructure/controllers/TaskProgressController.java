package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskProgress.GetAllTaskProgress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks/progress")
public class TaskProgressController {
    private final GetAllTaskProgress getAllTaskProgress;

    public TaskProgressController(GetAllTaskProgress getAllTaskProgress) {
        this.getAllTaskProgress = getAllTaskProgress;
    }

    @GetMapping
    public ResponseEntity<List<TaskProgress>> getAllTaskProgress(Principal principal) {
        if(principal == null || principal.getName() == null) { return ResponseEntity.status(401).build(); }
        List<TaskProgress> allProgress = this.getAllTaskProgress.execute(principal.getName());
        return ResponseEntity.ok(allProgress);
    }
}