package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;

import java.util.List;

public class GetTasksForLesson {
    private final TaskGateway taskGateway;

    public GetTasksForLesson(TaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }

    public List<Task> execute(String lessonId) {
        return this.taskGateway.getTasksByLessonId(lessonId);
    }
}