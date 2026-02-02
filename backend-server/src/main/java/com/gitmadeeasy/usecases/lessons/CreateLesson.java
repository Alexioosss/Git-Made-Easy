package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonDifficultyNotRecognisedException;
import com.gitmadeeasy.usecases.users.exceptions.MissingRequiredFieldException;

public class CreateLesson {
    private final LessonGateway lessonGateway;

    public CreateLesson(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(CreateLessonRequest request) {
        if(request.title() == null || request.title().isBlank()) {
            throw new MissingRequiredFieldException("title cannot be left blank");
        }

        if(request.description() == null || request.description().isBlank()) {
            throw new MissingRequiredFieldException("task description cannot be left blank");
        }

        if(request.difficulty() == null || request.difficulty().isBlank()) {
            throw new MissingRequiredFieldException("task difficulty cannot be left blank");
        }

        LessonDifficulty difficulty;
        try {
            difficulty = LessonDifficulty.valueOf(
                    request.difficulty().toUpperCase()
            );
        } catch(IllegalArgumentException e) {
            throw new LessonDifficultyNotRecognisedException(request.difficulty());
        }

        Lesson newLesson = new Lesson(
                request.title(), request.description(),
                difficulty
        );
        return this.lessonGateway.createLesson(newLesson);
    }
}