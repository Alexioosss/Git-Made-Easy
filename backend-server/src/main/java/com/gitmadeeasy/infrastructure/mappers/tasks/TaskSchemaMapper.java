package com.gitmadeeasy.infrastructure.mappers.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;

public class TaskSchemaMapper {

    public TaskSchema toSchema(Task lesson) {
        return new TaskSchema(
                lesson.getLessonId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getExpectedCommand(),
                lesson.getHint()
        );
    }

    public Task toEntity(TaskSchema taskSchema) {
        return new Task(
                taskSchema.getId(),
                taskSchema.getLessonId(),
                taskSchema.getTitle(),
                taskSchema.getContent(),
                taskSchema.getExpectedCommand(),
                taskSchema.getHint()
        );
    }
}