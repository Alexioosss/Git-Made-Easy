package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;

public class GetLessonById {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;

    public GetLessonById(LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    public Lesson execute(String lessonId) {
        Lesson lesson = this.lessonGateway.getLessonById(lessonId)
                .orElseThrow(() -> new LessonNotFoundWithIdException(lessonId));
        lesson.setTasks(this.taskGateway.getTasksByLessonId(lessonId));
        return lesson;
    }
}