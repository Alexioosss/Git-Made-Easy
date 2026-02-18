package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;

import java.util.List;

public class GetAllLessons {
    private final LessonGateway lessonGateway;

    public GetAllLessons(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public List<Lesson> execute() {
        return this.lessonGateway.findAllLessons();
    }
}