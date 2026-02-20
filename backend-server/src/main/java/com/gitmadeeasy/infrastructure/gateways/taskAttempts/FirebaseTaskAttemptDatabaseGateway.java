package com.gitmadeeasy.infrastructure.gateways.taskAttempts;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.firebase.FirebaseTaskAttemptRepository;
import com.gitmadeeasy.infrastructure.mappers.taskAttempts.TaskAttemptSchemaMapper;

import java.util.List;
import java.util.Optional;

public class FirebaseTaskAttemptDatabaseGateway implements TaskAttemptGateway {
    private final FirebaseTaskAttemptRepository firebase;
    private final TaskAttemptSchemaMapper mapper;

    public FirebaseTaskAttemptDatabaseGateway(FirebaseTaskAttemptRepository firebase, TaskAttemptSchemaMapper mapper) {
        this.firebase = firebase;
        this.mapper = mapper;
    }

    @Override
    public TaskProgress save(TaskProgress progress) {
        return this.mapper.fromFirebaseSchema(
                this.firebase.save(this.mapper.toFirebaseSchema(progress))
        );
    }

    @Override
    public Optional<TaskProgress> findByUserIdAndTaskId(String userId, String taskId) {
        return this.firebase.findByUserIdAndTaskId(userId, taskId).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public int countCompletedTasks(String userId, String lessonId) {
        return this.firebase.countCompletedTasks(userId, lessonId);
    }

    @Override
    public List<TaskProgress> findAllByUserId(String userId) {
        return this.firebase.findAllByUserId(userId).stream().map(this.mapper::fromFirebaseSchema).toList();
    }
}