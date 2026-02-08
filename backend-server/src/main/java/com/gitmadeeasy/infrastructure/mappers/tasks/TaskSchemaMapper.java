package com.gitmadeeasy.infrastructure.mappers.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;

public class TaskSchemaMapper {

    public TaskSchema toSchema(Task task) {
        return new TaskSchema(
                task.getLessonId(),
                task.getTitle(),
                task.getContent(),
                task.getExpectedCommand(),
                task.getHint(),
                task.getTaskOrder()
        );
    }

    public Task toEntity(TaskSchema taskSchema) {
        return new Task(
                taskSchema.getId(),
                taskSchema.getLessonId(),
                taskSchema.getTitle(),
                taskSchema.getContent(),
                taskSchema.getExpectedCommand(),
                taskSchema.getHint(),
                taskSchema.getTaskOrder()
        );
    }
}