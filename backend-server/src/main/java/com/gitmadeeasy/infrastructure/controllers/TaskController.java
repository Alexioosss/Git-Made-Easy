package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.usecases.tasks.CreateTask;
import com.gitmadeeasy.usecases.tasks.GetTaskById;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons/{lessonId}/tasks")
public class TaskController {
    private final CreateTask createTask;
    private final GetTaskById getTaskById;

    public TaskController(CreateTask createTask, GetTaskById getTaskById) {
        this.createTask = createTask;
        this.getTaskById = getTaskById;
    }

    @PostMapping("")
    public ResponseEntity<Task> createTask(@PathVariable("lessonId") String lessonId,
                                           @RequestBody CreateTaskRequest request) {
        Task newTask = this.createTask.execute(lessonId, request);
        return ResponseEntity.ok(newTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable("lessonId") String lessonId,
                                            @PathVariable("taskId") String taskId) {
        Task task = this.getTaskById.execute(lessonId, taskId);
        return ResponseEntity.ok(task);
    }
}