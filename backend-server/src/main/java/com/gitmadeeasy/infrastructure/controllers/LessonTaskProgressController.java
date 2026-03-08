package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.usecases.taskProgress.AttemptTask;
import com.gitmadeeasy.usecases.taskProgress.GetTaskProgress;
import com.gitmadeeasy.usecases.taskProgress.SyncTaskProgress;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskProgressUpdateRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController @RequestMapping("/lessons/{lessonId}/tasks")
public class LessonTaskProgressController {
    private final AttemptTask attemptTask;
    private final GetTaskProgress getTaskProgress;
    private final SyncTaskProgress syncTaskProgress;
    private static final Logger log = LoggerFactory.getLogger(LessonTaskProgressController.class);

    public LessonTaskProgressController(AttemptTask attemptTask, GetTaskProgress getTaskProgress,
                                        SyncTaskProgress syncTaskProgress) {
        this.attemptTask = attemptTask;
        this.getTaskProgress = getTaskProgress;
        this.syncTaskProgress = syncTaskProgress;
    }

    @PostMapping("/{taskId}/progress")
    public ResponseEntity<TaskProgress> recordTaskAttempt(@PathVariable("lessonId") String lessonId,
                                                          @PathVariable("taskId") String taskId, Principal principal,
                                                          @Valid @RequestBody TaskAttemptRequest request) {
        log.info("POST /lessons/{}/tasks/{}/progress - Saving task attempt", lessonId, taskId);
        TaskProgress progress = this.attemptTask.attempt(principal.getName(), lessonId, taskId, request);
        log.info("Task attempt recorded successfully. User ID {}, Task ID {}", progress.getUserId(), progress.getTaskId());
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/{taskId}/progress")
    public ResponseEntity<TaskProgress> getTaskAttempt(@PathVariable("lessonId") String lessonId,
                                                       @PathVariable("taskId") String taskId, Principal principal) {
        log.info("GET /lessons/{}/tasks/{}/progress - Fetching task attempt", lessonId, taskId);
        Optional<TaskProgress> progress = this.getTaskProgress.execute(principal.getName(), lessonId, taskId);
        log.info("Task attempt retrieved successfully");
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/progress/sync")
    public ResponseEntity<List<TaskProgress>> syncTaskProgress(@PathVariable("lessonId") String lessonId, Principal principal,
                                                         @Valid @RequestBody List<TaskProgressUpdateRequest> request) {
        log.info("POST /lessons/{}/tasks/progress/sync - Syncing progress to data store", lessonId);
        List<TaskProgress> syncedTasks = this.syncTaskProgress.syncProgress(principal.getName(), lessonId, request);
        log.info("Task progresses synced successfully.");
        return ResponseEntity.ok(syncedTasks);
    }
}