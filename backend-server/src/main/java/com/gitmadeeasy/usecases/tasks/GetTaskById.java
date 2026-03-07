package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetTaskById {
    private final TaskGateway taskGateway;
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(GetTaskById.class);

    public GetTaskById(TaskGateway taskGateway, LessonGateway lessonGateway) {
        this.taskGateway = taskGateway;
        this.lessonGateway = lessonGateway;
    }

    public Task execute(String lessonId, String taskId) {
        if(!this.lessonGateway.existsById(lessonId)) {
            log.warn("GetTaskById failed: lesson does not exist by ID {}", lessonId);
            throw new LessonNotFoundWithIdException(lessonId);
        }

        return this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));
    }
}