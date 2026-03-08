package com.gitmadeeasy.usecases.taskProgress;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;

import java.util.List;

public class GetAllTaskProgress {
    private final TaskAttemptGateway taskAttemptGateway;

    public GetAllTaskProgress(TaskAttemptGateway taskAttemptGateway) {
        this.taskAttemptGateway = taskAttemptGateway;
    }

    public List<TaskProgress> execute(String userId) {
        return this.taskAttemptGateway.findAllByUserId(userId);
    }
}