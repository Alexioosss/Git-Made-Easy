package com.gitmadeeasy.usecases.taskAttempt;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.taskAttempt.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotInLessonException;

public class TaskAttempt {
    private final TaskAttemptGateway taskAttemptGateway;
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;

    public TaskAttempt(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    public TaskProgress execute(String userId, String lessonId, String taskId, TaskAttemptRequest request) {
        if(!this.lessonGateway.existsById(lessonId)) {
            throw new LessonNotFoundWithIdException(lessonId);

        }
        Task task = this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));

        if(!task.getLessonId().equals(lessonId)) {
            throw new TaskNotInLessonException(taskId, lessonId);
        }

        TaskProgress taskProgress = this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)
                .orElse(new TaskProgress(null, userId, taskId));

        if(taskProgress.getStatus() == TaskCompletionStatus.COMPLETED) {
            taskProgress.recordAttempt(request.input());
            return this.taskAttemptGateway.save(taskProgress);
        }

        taskProgress.recordAttempt(request.input());
        if(request.input().equals(task.getExpectedCommand())) { taskProgress.markCompleted(); }
        else { taskProgress.markFailed("Incorrect Answer"); }

        return this.taskAttemptGateway.save(taskProgress);
    }
}