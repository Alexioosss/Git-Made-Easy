package com.gitmadeeasy.usecases.taskProgress;

import com.gitmadeeasy.entities.enums.TaskCompletionStatus;
import com.gitmadeeasy.entities.taskAttempts.TaskAttemptGateway;
import com.gitmadeeasy.entities.taskAttempts.TaskProgress;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessonProgress.LessonProgressFacade;
import com.gitmadeeasy.usecases.taskProgress.dto.TaskProgressUpdateRequest;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SyncTaskProgress {
    private final TaskAttemptGateway taskProgressGateway;
    private final TaskGateway taskGateway;
    private final LessonProgressFacade lessonProgressFacade;
    private static final Logger log = LoggerFactory.getLogger(SyncTaskProgress.class);

    public SyncTaskProgress(TaskAttemptGateway taskProgressGateway, TaskGateway taskGateway, LessonProgressFacade lessonProgressFacade) {
        this.taskProgressGateway = taskProgressGateway;
        this.taskGateway = taskGateway;
        this.lessonProgressFacade = lessonProgressFacade;
    }

    public List<TaskProgress> syncProgress(String userId, String lessonId, List<TaskProgressUpdateRequest> progressUpdates) {
        log.info("Updating User ID {}'s task progress, existing if applicable, or new.", userId);
        return progressUpdates.stream().map(update -> {
            Task task = this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, update.taskId())
                    .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, update.taskId()));

            TaskProgress existingProgress =
                    this.taskProgressGateway.findByUserIdAndTaskId(userId, update.taskId()).orElse(null);
            if(existingProgress != null) { log.info("Found existing Task Progress for user with ID {}.", userId); }

            LocalDate startedAt = update.startedAt() != null ?
                    OffsetDateTime.parse(update.startedAt()).toLocalDate() : LocalDate.now();
            LocalDate completedAt = OffsetDateTime.parse(update.completedAt()).toLocalDate();

            TaskProgress progress = new TaskProgress(
                    existingProgress != null ? existingProgress.getTaskProgressId() : null,
                    userId, update.taskId(), lessonId, task.getTitle(),
                    update.status() != null ? TaskCompletionStatus.valueOf(update.status()) : TaskCompletionStatus.IN_PROGRESS,
                    update.attempts() != null ? update.attempts() : 0,
                    update.lastInput(), update.lastError() != null ? update.lastError() : "", startedAt, completedAt);

            TaskProgress savedProgress = this.taskProgressGateway.save(progress);
            log.info("Updated task progress saved. Task ID {}, Task Completion Status: {}", savedProgress.getTaskId(), savedProgress.getStatus());
            this.lessonProgressFacade.update(userId, lessonId, savedProgress);
            return savedProgress;
        }).collect(Collectors.toList());
    }
}