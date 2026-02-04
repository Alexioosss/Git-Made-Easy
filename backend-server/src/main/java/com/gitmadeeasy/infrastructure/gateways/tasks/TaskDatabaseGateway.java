package com.gitmadeeasy.infrastructure.gateways.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.TaskRepository;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;

import java.util.List;
import java.util.Optional;

public class TaskDatabaseGateway implements TaskGateway {
    private final TaskRepository taskRepository;
    private final TaskSchemaMapper taskSchemaMapper;

    public TaskDatabaseGateway(TaskRepository taskRepository, TaskSchemaMapper taskSchemaMapper) {
        this.taskRepository = taskRepository;
        this.taskSchemaMapper = taskSchemaMapper;
    }

    @Override
    public Task createTask(Task newTask) {
        TaskSchema savedTaskSchema = this.taskRepository.save(this.taskSchemaMapper.toSchema(newTask));
        return this.taskSchemaMapper.toEntity(savedTaskSchema);
    }

    @Override
    public Optional<Task> getTaskByLessonIdAndTaskId(String lessonId, String taskId) {
        return this.taskRepository.findByLessonIdAndTaskId(lessonId, taskId)
                .map(this.taskSchemaMapper::toEntity);
    }

    @Override
    public List<Task> getTasksByLessonId(String lessonId) {
        return this.taskRepository.findAllByLessonId(lessonId)
                .stream().map(this.taskSchemaMapper::toEntity).toList();
    }

    @Override
    public boolean existsById(String taskId) {
        return this.taskRepository.existsById(taskId);
    }
}