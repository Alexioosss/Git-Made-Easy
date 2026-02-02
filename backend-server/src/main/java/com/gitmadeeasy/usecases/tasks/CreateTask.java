package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;

public class CreateTask {
    private final TaskGateway taskGateway;
    private final LessonGateway lessonGateway;

    public CreateTask(TaskGateway taskGateway, LessonGateway lessonGateway) {
        this.taskGateway = taskGateway;
        this.lessonGateway = lessonGateway;
    }

    public Task execute(String lessonId, CreateTaskRequest createTaskRequest) {
        if(!this.lessonGateway.existsById(lessonId)) { throw new LessonNotFoundWithIdException(lessonId); }

        Task newTask = new Task(
                lessonId,
                createTaskRequest.title(),
                createTaskRequest.content(),
                createTaskRequest.expectedCommand(),
                createTaskRequest.hint()
        );

        return this.taskGateway.createTask(newTask);
    }
}