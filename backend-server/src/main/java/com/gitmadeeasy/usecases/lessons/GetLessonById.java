package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonNotFoundWithIdException;

import java.util.Optional;

public class GetLessonById {
    private final LessonGateway lessonGateway;

    public GetLessonById(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(String lessonId) {
        return this.lessonGateway.getLessonById(lessonId)
                .orElseThrow(() -> new LessonNotFoundWithIdException(lessonId));
    }
}