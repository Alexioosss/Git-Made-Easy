package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

public class ConcreteJpaTaskRepository implements TaskRepository {
    private final AbstractJpaTaskRepository jpa;

    public ConcreteJpaTaskRepository(AbstractJpaTaskRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public TaskSchema save(TaskSchema taskSchema) {
        return this.jpa.save(taskSchema);
    }

    @Override
    public Optional<TaskSchema> findByLessonIdAndTaskId(String lessonId, String taskId) {
        return this.jpa.findByLessonIdAndId(Long.valueOf(lessonId), Long.valueOf(taskId));
    }

    @Override
    public List<TaskSchema> findAllByLessonId(String lessonId) {
        return this.jpa.findAllByLessonId(Long.valueOf(lessonId));
    }

    @Override
    public boolean existsById(String taskId) {
        return this.jpa.existsById(Long.valueOf(taskId));
    }
}