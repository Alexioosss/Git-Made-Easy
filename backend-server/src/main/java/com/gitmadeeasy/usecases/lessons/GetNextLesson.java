package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;

public class GetNextLesson {
    private final LessonGateway lessonGateway;

    public GetNextLesson(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(String lessonId) {
        Lesson currentLesson = this.lessonGateway.getLessonById(lessonId)
                .orElseThrow(() -> new LessonNotFoundWithIdException(lessonId));
        return this.lessonGateway.getNextLesson(currentLesson.getLessonOrder());
    }
}