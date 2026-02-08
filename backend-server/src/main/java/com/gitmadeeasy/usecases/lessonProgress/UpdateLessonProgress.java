package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.TaskGateway;

public class UpdateLessonProgress {
    private final LessonProgressGateway lessonProgressGateway;
    private final TaskAttemptGateway taskAttemptGateway;
    private final TaskGateway taskGateway;

    public UpdateLessonProgress(LessonProgressGateway lessonProgressGateway, TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway) {
        this.lessonProgressGateway = lessonProgressGateway;
        this.taskAttemptGateway = taskAttemptGateway;
        this.taskGateway = taskGateway;
    }

    public void update(String userId, String lessonId, TaskProgress taskProgress) {
        this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId).ifPresentOrElse(existing -> {
            existing.setCurrentTaskProgressId(taskProgress.getTaskProgressId());
            int completed = this.taskAttemptGateway.countCompletedTasks(userId, lessonId);
            int total = this.taskGateway.countTasksInLesson(lessonId);
            existing.updateTasksCounts(completed, total);
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