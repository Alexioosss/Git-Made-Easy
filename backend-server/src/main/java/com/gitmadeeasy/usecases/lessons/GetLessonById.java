package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetLessonById {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;
    private static final Logger log = LoggerFactory.getLogger(GetLessonById.class);

    public GetLessonById(LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    public Lesson execute(String lessonId) {
        Lesson lesson = this.lessonGateway.getLessonById(lessonId)
                .orElseThrow(() -> new LessonNotFoundWithIdException(lessonId));
        log.info("Lesson found successfully by its id");
        lesson.setTasks(this.taskGateway.getTasksByLessonId(lessonId));
        log.info("Lesson tasks have been populated (if any exist for lesson) successfully.");
        return lesson;
    }
}