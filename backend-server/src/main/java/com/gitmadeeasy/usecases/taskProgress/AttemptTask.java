package com.gitmadeeasy.usecases.taskProgress;

import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskAttemptRequest;
import com.gitmadeeasy.usecases.lessonProgress.LessonProgressFacade;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttemptTask {
    private final TaskAttemptGateway taskAttemptGateway;
    private final TaskGateway taskGateway;
    private final LessonProgressFacade lessonProgressFacade;
    private static final Logger log = LoggerFactory.getLogger(AttemptTask.class);

    public AttemptTask(TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway,
                       LessonProgressFacade lessonProgressFacade) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.taskGateway = taskGateway;
        this.lessonProgressFacade = lessonProgressFacade;
    }

    public TaskProgress attempt(String userId, String lessonId, String taskId, TaskAttemptRequest request) {
        // Validate task exists
        Task task = this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));
        log.info("Task found successfully for Lesson ID {}", lessonId);

        // Find an existing task progress for the user, or create a new one
        TaskProgress taskProgress = this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)
                .orElse(new TaskProgress(null, userId, taskId, lessonId, task.getTitle()));
        log.info("Task progress found for User ID {}", userId);

        // Attempt the task, check if input matches expected command, increase number of attempts, mark task as completed if correct
        taskProgress.attempt(task, request.input());

        TaskProgress savedTaskProgress = this.taskAttemptGateway.save(taskProgress);
        log.info("Saved user's task attempt");
        this.lessonProgressFacade.update(userId, lessonId, savedTaskProgress);
        log.info("Updating user progress on Lesson ID {}", lessonId);
        return savedTaskProgress;
    }
}