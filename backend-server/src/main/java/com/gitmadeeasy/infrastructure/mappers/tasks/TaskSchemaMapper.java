package com.gitmadeeasy.infrastructure.mappers.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.infrastructure.gateways.tasks.FirebaseTaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;

public class TaskSchemaMapper {

    public JpaTaskSchema toJpaSchema(Task task) {
        JpaTaskSchema schema =  new JpaTaskSchema(
                task.getLessonId(),
                task.getTitle(),
                task.getContent(),
                task.getExpectedCommand(),
                task.getHint(),
                task.getTaskOrder()
        );
        if(task.getTaskId() != null) { schema.setId(task.getTaskId()); }
        return schema;
    }

    public Task fromJpaSchema(JpaTaskSchema taskSchema) {
        return new Task(
                String.valueOf(taskSchema.getId()),
                String.valueOf(taskSchema.getLessonId()),
                taskSchema.getTitle(),
                taskSchema.getContent(),
                taskSchema.getExpectedCommand(),
                taskSchema.getHint(),
                taskSchema.getTaskOrder()
        );
    }

    public FirebaseTaskSchema toFirebaseSchema(Task task) {
        FirebaseTaskSchema schema = new FirebaseTaskSchema(
                task.getLessonId(),
                task.getTitle(),
                task.getContent(),
                task.getExpectedCommand(),
                task.getHint(),
                task.getTaskOrder()
        );
        if(task.getTaskId() != null) { schema.setId(task.getTaskId()); }
        return schema;
    }

    public Task fromFirebaseSchema(FirebaseTaskSchema schema) {
        return new Task(
                schema.getId(),
                schema.getLessonId(),
                schema.getTitle(),
                schema.getContent(),
                schema.getExpectedCommand(),
                schema.getHint(),
                schema.getTaskOrder()
        );
    }
}