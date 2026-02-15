package com.gitmadeeasy.infrastructure.gateways.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.infrastructure.gateways.tasks.repositories.firebase.FirebaseTaskRepository;
import com.gitmadeeasy.infrastructure.mappers.tasks.TaskSchemaMapper;

import java.util.List;
import java.util.Optional;

public class FirebaseTaskDatabaseGateway implements TaskGateway {
    private final FirebaseTaskRepository firebase;
    private final TaskSchemaMapper mapper;

    public FirebaseTaskDatabaseGateway(FirebaseTaskRepository firebase, TaskSchemaMapper mapper) {
        this.firebase = firebase;
        this.mapper = mapper;
    }

    @Override
    public Task createTask(Task newTask) {
        return this.mapper.fromFirebaseSchema(
                this.firebase.save(this.mapper.toFirebaseSchema(newTask))
        );
    }

    @Override
    public Optional<Task> getTaskByLessonIdAndTaskId(String lessonId, String taskId) {
        return this.firebase.findByLessonIdAndTaskId(lessonId, taskId).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public List<Task> getTasksByLessonId(String lessonId) {
        return this.firebase.findAllByLessonId(lessonId).stream().map(this.mapper::fromFirebaseSchema).toList();
    }

    @Override
    public boolean existsById(String taskId) {
        return this.firebase.existsById(taskId);
    }

    @Override
    public int getNextTaskOrderForLesson(String lessonId) {
        return this.firebase.findMaxTaskOrderByLessonId(lessonId) + 1;
    }

    @Override
    public int countTasksInLesson(String lessonId) {
        return this.firebase.countTasksByLessonId(lessonId);
    }
}