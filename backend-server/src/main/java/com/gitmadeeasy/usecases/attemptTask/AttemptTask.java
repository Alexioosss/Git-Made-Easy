package com.gitmadeeasy.usecases.attemptTask;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.attemptTask.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.lessonProgress.UpdateLessonProgress;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;

public class AttemptTask {
    private final TaskAttemptGateway taskAttemptGateway;
    private final TaskGateway taskGateway;
    private final UpdateLessonProgress updateLessonProgress;

    public AttemptTask(TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway,
                       UpdateLessonProgress updateLessonProgress) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.taskGateway = taskGateway;
        this.updateLessonProgress = updateLessonProgress;
    }

    public TaskProgress attempt(String userId, String lessonId, String taskId, TaskAttemptRequest request) {
        // Validate task exists
        Task task = this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));

        // Find an existing task progress for the user, or create a new one
        TaskProgress taskProgress = this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)
                .orElse(new TaskProgress(null, userId, taskId));

        // Attempt the task, check if input matches expected command, increase number of attempts, mark task as completed if correct
        taskProgress.attempt(task, request.input());

        TaskProgress savedTaskProgress = this.taskAttemptGateway.save(taskProgress);
        updateLessonProgress.update(userId, lessonId, savedTaskProgress);
        return savedTaskProgress;
    }
}