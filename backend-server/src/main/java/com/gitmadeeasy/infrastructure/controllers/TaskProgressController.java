package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskAttempt.GetTaskProgress;
import com.gitmadeeasy.usecases.taskAttempt.TaskAttempt;
import com.gitmadeeasy.usecases.taskAttempt.dto.TaskAttemptRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/lessons/{lessonId}/tasks/{taskId}/progress")
public class TaskProgressController {
    private final TaskAttempt taskAttempt;
    private final GetTaskProgress getTaskProgress;

    public TaskProgressController(TaskAttempt taskAttempt, GetTaskProgress getTaskProgress) {
        this.taskAttempt = taskAttempt;
        this.getTaskProgress = getTaskProgress;
    }

    @PostMapping("")
    public ResponseEntity<TaskProgress> recordTaskAttempt(
            @PathVariable("lessonId") String lessonId,
            @PathVariable("taskId") String taskId,
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody TaskAttemptRequest request) {

        TaskProgress progress = this.taskAttempt.execute(userId, lessonId, taskId, request);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("")
    public ResponseEntity<TaskProgress> getTaskAttempt(
            @PathVariable("lessonId") String lessonId,
            @PathVariable("taskId") String taskId,
            @AuthenticationPrincipal String userId) {

        Optional<TaskProgress> progress = this.getTaskProgress.execute(userId, lessonId, taskId);
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}