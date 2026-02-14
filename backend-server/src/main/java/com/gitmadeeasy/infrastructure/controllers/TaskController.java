package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.dto.tasks.TaskResponse;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskResponseMapper;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/lessons/{lessonId}/tasks")
public class TaskController {
    private final CreateTask createTask;
    private final GetTaskById getTaskById;
    private final TaskResponseMapper mapper;

    public TaskController(CreateTask createTask, GetTaskById getTaskById, TaskResponseMapper mapper) {
        this.createTask = createTask;
        this.getTaskById = getTaskById;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@PathVariable("lessonId") String lessonId,
                                                   @Valid @RequestBody CreateTaskRequest request) {
        Task newTask = this.createTask.execute(lessonId, request);
        return ResponseEntity.created(URI.create("/lessons/" + lessonId + "/tasks/" + newTask.getTaskId()))
                .body(this.mapper.toResponse(newTask));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("lessonId") String lessonId,
                                            @PathVariable("taskId") String taskId) {
        Task foundTask = this.getTaskById.execute(lessonId, taskId);
        return ResponseEntity.ok(this.mapper.toResponse(foundTask));
    }
}