package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskAttempt.TaskAttempt;
import com.gitmadeeasy.usecases.taskAttempt.dto.TaskAttemptRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lessons/{lessonId}/tasks/{taskId}/progress")
public class TaskProgressController {
    private final TaskAttempt taskAttempt;

    public TaskProgressController(TaskAttempt taskAttempt) {
        this.taskAttempt = taskAttempt;
    }

    @PostMapping("")
    public ResponseEntity<TaskProgress> recordTaskAttempt(
            @PathVariable("lessonId") String lessonId,
            @PathVariable("taskId") String taskId,
            @AuthenticationPrincipal String userId,
            @RequestBody TaskAttemptRequest request) {
        TaskProgress progress = this.taskAttempt.execute(
                userId, lessonId, taskId, request
        );
        return ResponseEntity.ok(progress);
    }
}