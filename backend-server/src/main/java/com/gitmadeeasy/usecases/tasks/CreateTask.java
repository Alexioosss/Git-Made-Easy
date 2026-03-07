package com.gitmadeeasy.usecases.tasks;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.DifficultyLevelNotRecognisedException;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import com.gitmadeeasy.usecases.tasks.dto.CreateTaskRequest;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTask {
    private final TaskGateway taskGateway;
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(CreateTask.class);

    public CreateTask(TaskGateway taskGateway, LessonGateway lessonGateway) {
        this.taskGateway = taskGateway;
        this.lessonGateway = lessonGateway;
    }

    public Task execute(String lessonId, CreateTaskRequest request) {
        if(!this.lessonGateway.existsById(lessonId)) {
            log.warn("CreateTask failed: lesson does not exist with ID{}", lessonId);
            throw new LessonNotFoundWithIdException(lessonId);
        }

        if(request.title() == null || request.title().isBlank()) {
            log.warn("CreateTask failed: missing title field from request");
            throw new MissingRequiredFieldException("task title cannot be left blank");
        }

        if(request.content() == null || request.content().isBlank()) {
            log.warn("CreateTask failed: missing content field from request");
            throw new MissingRequiredFieldException("task content cannot be left blank");
        }

        if(request.expectedCommand() == null || request.expectedCommand().isBlank()) {
            log.warn("CreateTask failed: missing expectedCommand field from request");
            throw new MissingRequiredFieldException("expected command cannot be left blank");
        }

        Integer taskOrder = request.taskOrder() != null && request.taskOrder() >= 0
                ? request.taskOrder() : this.taskGateway.getNextTaskOrderForLesson(lessonId);
        log.info("Task order has been produced for current task");

        DifficultyLevels taskDifficulty;
        try { taskDifficulty = DifficultyLevels.valueOf(request.taskDifficulty().toUpperCase()); }
        catch(IllegalArgumentException e) { throw new DifficultyLevelNotRecognisedException(request.taskDifficulty()); }

        Task newTask = new Task(
                lessonId, request.title(), request.content(), request.expectedCommand(),
                request.hint(), taskOrder, taskDifficulty);

        return this.taskGateway.createTask(newTask);
    }
}