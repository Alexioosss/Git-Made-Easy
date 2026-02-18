package com.gitmadeeasy.infrastructure.mappers.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.dto.tasks.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskResponseMapper {
    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getTaskId(),
                task.getLessonId(),
                task.getTitle(),
                task.getContent(),
                task.getExpectedCommand(),
                task.getHint(),
                task.getTaskOrder(),
                task.getDifficulty().name());
    }
}