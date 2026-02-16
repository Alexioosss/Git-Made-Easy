package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.attemptTask.AttemptTask;
import com.gitmadeeasy.usecases.attemptTask.GetTaskProgress;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController @RequestMapping("/lessons/{lessonId}/tasks/{taskId}/progress")
public class TaskProgressController {
    private final AttemptTask attemptTask;
    private final GetTaskProgress getTaskProgress;
    private static final Logger log = LoggerFactory.getLogger(TaskProgressController.class);

    public TaskProgressController(AttemptTask attemptTask, GetTaskProgress getTaskProgress) {
        this.attemptTask = attemptTask;
        this.getTaskProgress = getTaskProgress;
    }

    @PostMapping
    public ResponseEntity<TaskProgress> recordTaskAttempt(@PathVariable("lessonId") String lessonId,
                                                          @PathVariable("taskId") String taskId, Principal principal,
                                                          @Valid @RequestBody TaskAttemptRequest request) {
        log.info("POST /lessons/{}/tasks/{}/progress - Saving task attempt", lessonId, taskId);
        TaskProgress progress = this.attemptTask.attempt(principal.getName(), lessonId, taskId, request);
        log.info("Task attempt recorded successfully. UserID={}, taskID={}", progress.getUserId(), progress.getTaskId());
        return ResponseEntity.ok(progress);
    }

    @GetMapping
    public ResponseEntity<TaskProgress> getTaskAttempt(@PathVariable("lessonId") String lessonId,
                                                       @PathVariable("taskId") String taskId, Principal principal) {
        log.info("GET /lessons/{}/tasks/{}/progress - Fetching task attempt", lessonId, taskId);
        Optional<TaskProgress> progress = this.getTaskProgress.execute(principal.getName(), lessonId, taskId);
        log.info("Task attempt retrieved successfully");
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}