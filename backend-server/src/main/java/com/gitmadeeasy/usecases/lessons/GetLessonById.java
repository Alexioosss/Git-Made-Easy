package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class GetLessonById {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;
    private static final Logger log = LoggerFactory.getLogger(GetLessonById.class);

    public GetLessonById(LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    public Lesson execute(String lessonId) {
        Lesson lesson = this.lessonGateway.getLessonById(lessonId).orElseThrow(() -> new LessonNotFoundWithIdException(lessonId));
        log.info("Lesson found successfully by its id");

        List<Task> tasks = this.taskGateway.getTasksByIds(lesson.getTaskIds());
        tasks = tasks.stream().sorted(Comparator.comparing(Task::getTaskOrder)).toList();
        lesson.setTasks(tasks);

        if(!lesson.getTasks().isEmpty()) { log.info("Lesson tasks have been populated successfully."); }
        else { log.info("No lesson tasks have been populated. No tasks exist for this lesson."); }

        return lesson;
    }
}