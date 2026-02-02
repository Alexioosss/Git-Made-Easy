package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.exceptions.TaskNotFoundWithIdException;

public class GetTaskById {
    private final TaskGateway taskGateway;
    private final LessonGateway lessonGateway;

    public GetTaskById(TaskGateway taskGateway, LessonGateway lessonGateway) {
        this.taskGateway = taskGateway;
        this.lessonGateway = lessonGateway;
    }

    public Task execute(String lessonId, String taskId) {
        if(!this.lessonGateway.existsById(lessonId)) { throw new LessonNotFoundWithIdException(lessonId); }
        return this.taskGateway.getTaskByLessonIdAndTaskId(lessonId, taskId)
                .orElseThrow(() -> new TaskNotFoundWithIdException(lessonId, taskId));
    }
}