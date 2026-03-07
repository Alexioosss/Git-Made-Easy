package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.entities.tasks.Task;
import com.gitmadeeasy.entities.tasks.TaskGateway;

import java.util.List;
import java.util.Optional;

public class GetAllLessons {
    private final LessonGateway lessonGateway;
    private final TaskGateway taskGateway;

    public GetAllLessons(LessonGateway lessonGateway, TaskGateway taskGateway) {
        this.lessonGateway = lessonGateway;
        this.taskGateway = taskGateway;
    }

    public List<Lesson> execute() {
        List<Lesson> lessons = this.lessonGateway.findAllLessons();
        for(Lesson lesson : lessons) {
            List<Task> tasks = lesson.getTaskIds().stream().map(this.taskGateway::findById).flatMap(Optional::stream).toList();
            lesson.setTasks(tasks);
        }
        return lessons;
    }
}