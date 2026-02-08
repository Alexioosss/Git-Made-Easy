package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.attemptTask.GetTaskProgress;
import com.gitmadeeasy.usecases.attemptTask.AttemptTask;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/lessons/{lessonId}/tasks/{taskId}/progress")
public class TaskProgressController {
    private final AttemptTask attemptTask;
    private final GetTaskProgress getTaskProgress;

    public TaskProgressController(AttemptTask attemptTask, GetTaskProgress getTaskProgress) {
        this.attemptTask = attemptTask;
        this.getTaskProgress = getTaskProgress;
    }

    @PostMapping("")
    public ResponseEntity<TaskProgress> recordTaskAttempt(
            @PathVariable("lessonId") String lessonId,
            @PathVariable("taskId") String taskId,
            @AuthenticationPrincipal(expression = "username") String userId,
            @Valid @RequestBody TaskAttemptRequest request) {

        TaskProgress progress = this.attemptTask.attempt(userId, lessonId, taskId, request);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("")
    public ResponseEntity<TaskProgress> getTaskAttempt(
            @PathVariable("lessonId") String lessonId,
            @PathVariable("taskId") String taskId,
            @AuthenticationPrincipal(expression = "username") String userId) {

        Optional<TaskProgress> progress = this.getTaskProgress.execute(userId, lessonId, taskId);
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}