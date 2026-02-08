package com.gitmadeeasy.usecases.taskAttempt;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
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
    private final LessonProgressGateway lessonProgressGateway;

    public TaskAttempt(TaskAttemptGateway taskAttemptGateway, LessonGateway lessonGateway, TaskGateway taskGateway,
                       LessonProgressGateway lessonProgressGateway) {
        this.taskAttemptGateway = taskAttemptGateway;
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
        this.lessonProgressGateway = lessonProgressGateway;
    }

    public TaskProgress execute(String userId, String lessonId, String taskId, TaskAttemptRequest request) {
        // Validate lesson exists
        if(!this.lessonGateway.existsById(lessonId)) { throw new LessonNotFoundWithIdException(lessonId); }

        // Validate task exists
        Task task = this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));

        // Validate task is for the correct lesson
        if(!task.getLessonId().equals(lessonId)) { throw new TaskNotInLessonException(taskId, lessonId); }

        // Find an existing task progress for the user, or create a new one
        TaskProgress taskProgress = this.taskAttemptGateway.findByUserIdAndTaskId(userId, taskId)
                .orElse(new TaskProgress(null, userId, taskId));

        // If the task is completed, just keep increasing the number of attempts taken on this task and save it
        if(taskProgress.getStatus() == TaskCompletionStatus.COMPLETED) {
            taskProgress.recordAttempt(request.input());
            return this.taskAttemptGateway.save(taskProgress);
        }

        // Save the new attempt, increase the number of attempts taken
        taskProgress.recordAttempt(request.input());
        // Check if the command matches exactly, if so, mark task COMPLETE, storing time / date of completion
        if(request.input().equals(task.getExpectedCommand())) { taskProgress.markCompleted(); }
        else { taskProgress.markFailed("Incorrect Answer"); } // Save an error message for incorrect input

        TaskProgress savedTaskProgress = this.taskAttemptGateway.save(taskProgress);
        updateLessonProgress(userId, lessonId, savedTaskProgress);
        return savedTaskProgress;
    }

    private void updateLessonProgress(String userId, String lessonId, TaskProgress taskProgress) {
        this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId).ifPresentOrElse(existing -> {
            existing.setCurrentTaskProgressId(taskProgress.getTaskProgressId());
            int completed = this.taskAttemptGateway.countCompletedTasks(userId, lessonId);
            int total = this.taskGateway.countTasksInLesson(lessonId);
            existing.setCompletedTasksCount(completed);
            existing.setTotalTasksCount(total);
            this.lessonProgressGateway.save(existing);
        }, () -> {
            int total = this.taskGateway.countTasksInLesson(lessonId);
            LessonProgress newProgress = new LessonProgress(
                    null, userId, lessonId, taskProgress.getTaskProgressId(),
                    taskProgress.getStatus() == TaskCompletionStatus.COMPLETED ? 1: 0,
                    total
            );
            this.lessonProgressGateway.save(newProgress);
        });
    }
}