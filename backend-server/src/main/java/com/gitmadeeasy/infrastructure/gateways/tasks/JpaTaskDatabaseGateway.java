package com.gitmadeeasy.infrastructure.gateways.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa.JpaTaskRepository;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;

import java.util.List;
import java.util.Optional;

public class JpaTaskDatabaseGateway implements TaskGateway {
    private final JpaTaskRepository jpa;
    private final TaskSchemaMapper taskSchemaMapper;

    public JpaTaskDatabaseGateway(JpaTaskRepository jpa, TaskSchemaMapper taskSchemaMapper) {
        this.jpa = jpa;
        this.taskSchemaMapper = taskSchemaMapper;
    }

    @Override
    public Task createTask(Task newTask) {
        return this.taskSchemaMapper.fromJpaSchema(this.jpa.save(this.taskSchemaMapper.toJpaSchema(newTask)));
    }

    @Override
    public Optional<Task> getTaskByLessonIdAndTaskId(String lessonId, String taskId) {
        return this.jpa.findByLessonIdAndId(lessonId, taskId).map(this.taskSchemaMapper::fromJpaSchema);
    }

    @Override
    public List<Task> getTasksByLessonId(String lessonId) {
        return this.jpa.findAllByLessonId(lessonId).stream().map(this.taskSchemaMapper::fromJpaSchema).toList();
    }

    @Override
    public boolean existsById(String taskId) {
        return this.jpa.existsById(taskId);
    }

    @Override
    public int getNextTaskOrderForLesson(String lessonId) {
        return this.jpa.findMaxTaskOrderByLessonId(lessonId) + 1;
    }

    @Override
    public int countTasksInLesson(String lessonId) {
        return this.jpa.findAllByLessonId(lessonId).size();
    }
}