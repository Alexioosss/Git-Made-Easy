package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.dto.tasks.TaskResponse;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskResponseMapper;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/lessons/{lessonId}/tasks")
public class TaskController {
    private final CreateTask createTask;
    private final GetTaskById getTaskById;
    private final TaskResponseMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    public TaskController(CreateTask createTask, GetTaskById getTaskById, TaskResponseMapper mapper) {
        this.createTask = createTask;
        this.getTaskById = getTaskById;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@PathVariable("lessonId") String lessonId,
                                                   @Valid @RequestBody CreateTaskRequest request) {
        log.info("POST /lessons/{}/tasks - Creating a new task", lessonId);
        Task createdTask = this.createTask.execute(lessonId, request);
        log.info("Task created successfully. TaskID= {}", createdTask.getTaskId());
        return ResponseEntity.created(URI.create("/lessons/" + lessonId + "/tasks/" + createdTask.getTaskId()))
                .body(this.mapper.toResponse(createdTask));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("lessonId") String lessonId,
                                            @PathVariable("taskId") String taskId) {
        log.info("GET /lessons/{}/tasks/{} - Fetching task with taskID= {} for lessonID= {}", lessonId, taskId, taskId, lessonId);
        Task foundTask = this.getTaskById.execute(lessonId, taskId);
        log.info("Task found successfully. TaskID= {}", foundTask.getTaskId());
        return ResponseEntity.ok(this.mapper.toResponse(foundTask));
    }
}