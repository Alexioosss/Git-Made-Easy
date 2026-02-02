package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonDifficultyNotRecognisedException;

public class CreateLesson {
    private final LessonGateway lessonGateway;

    public CreateLesson(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(CreateLessonRequest createLessonRequest) {
        LessonDifficulty difficulty;
        try {
            difficulty = LessonDifficulty.valueOf(
                    createLessonRequest.difficulty().toUpperCase()
            );
        } catch(IllegalArgumentException e) {
            throw new LessonDifficultyNotRecognisedException(createLessonRequest.difficulty());
        }

        Lesson newLesson = new Lesson(
                createLessonRequest.title(), createLessonRequest.description(),
                difficulty
        );
        return this.lessonGateway.createLesson(newLesson);
    }
}