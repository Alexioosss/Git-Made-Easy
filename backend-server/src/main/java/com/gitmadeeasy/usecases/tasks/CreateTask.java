package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import com.gitmadeeasy.usecases.users.exceptions.MissingRequiredFieldException;

public class CreateTask {
    private final TaskGateway taskGateway;
    private final LessonGateway lessonGateway;

    public CreateTask(TaskGateway taskGateway, LessonGateway lessonGateway) {
        this.taskGateway = taskGateway;
        this.lessonGateway = lessonGateway;
    }

    public Task execute(String lessonId, CreateTaskRequest request) {
        if(!this.lessonGateway.existsById(lessonId)) { throw new LessonNotFoundWithIdException(lessonId); }

        if(request.title() == null || request.title().isBlank()) {
            throw new MissingRequiredFieldException("task title cannot be left blank");
        }

        if(request.content() == null || request.content().isBlank()) {
            throw new MissingRequiredFieldException("task content cannot be left blank");
        }

        if(request.expectedCommand() == null || request.expectedCommand().isBlank()) {
            throw new MissingRequiredFieldException("expected command cannot be left blank");
        }

        Integer taskOrder = request.taskOrder() != null ? request.taskOrder() :
                this.taskGateway.getNextTaskOrderForLesson(lessonId);

        Task newTask = new Task(
                lessonId,
                request.title(),
                request.content(),
                request.expectedCommand(),
                request.hint(),
                taskOrder
        );

        return this.taskGateway.createTask(newTask);
    }
}