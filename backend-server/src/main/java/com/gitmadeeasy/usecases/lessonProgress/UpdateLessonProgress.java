package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateLessonProgress {
    private final LessonProgressGateway lessonProgressGateway;
    private final TaskAttemptGateway taskAttemptGateway;
    private final TaskGateway taskGateway;
    private static final Logger log = LoggerFactory.getLogger(UpdateLessonProgress.class);

    public UpdateLessonProgress(LessonProgressGateway lessonProgressGateway,
                                TaskAttemptGateway taskAttemptGateway, TaskGateway taskGateway) {
        this.lessonProgressGateway = lessonProgressGateway;
        this.taskAttemptGateway = taskAttemptGateway;
        this.taskGateway = taskGateway;
    }

    public void update(String userId, String lessonId, TaskProgress taskProgress) {
        this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId).ifPresentOrElse(
                existing -> { // A lesson progress already existed for the user, so update its fields
            log.info("Lesson progress already exists. Updating existing lesson progress for userID= {}", userId);

            existing.setCurrentTaskProgressId(taskProgress.getTaskProgressId());
            int completed = this.taskAttemptGateway.countCompletedTasks(userId, lessonId);
            int total = this.taskGateway.countTasksInLesson(lessonId);
            existing.updateTasksCounts(completed, total);
            this.lessonProgressGateway.save(existing);
            log.info("Lesson progress updated successfully.");
        }, () -> { // If no lesson progress was found for the user for this lesson, create a new lesson progress
            log.info("Lesson progress did not already exist. Creating new lesson progress for userID= {}", userId);

            int total = this.taskGateway.countTasksInLesson(lessonId);
            LessonProgress newProgress = new LessonProgress(
                    null, userId, lessonId, taskProgress.getTaskProgressId(),
                    taskProgress.getStatus() == TaskCompletionStatus.COMPLETED ? 1: 0,
                    total
            );
            this.lessonProgressGateway.save(newProgress);
            log.info("Saving new lesson progress for userID={}" , userId);
        });
    }
}