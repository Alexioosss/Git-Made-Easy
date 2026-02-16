package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.infrastructure.controllers.LessonController;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.LessonDifficultyNotRecognisedException;
import com.gitmadeeasy.usecases.validation.exceptions.MissingRequiredFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateLesson {
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(CreateLesson.class);

    public CreateLesson(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(CreateLessonRequest request) {
        if(request.title() == null || request.title().isBlank()) {
            log.warn("CreateLesson failed: missing title field from request");
            throw new MissingRequiredFieldException("title cannot be left blank");
        }

        if(request.description() == null || request.description().isBlank()) {
            log.warn("CreateLesson failed: missing description field from request");
            throw new MissingRequiredFieldException("task description cannot be left blank");
        }

        if(request.difficulty() == null || request.difficulty().isBlank()) {
            log.warn("CreateLesson failed: missing difficulty field from request");
            throw new MissingRequiredFieldException("task difficulty cannot be left blank");
        }

        LessonDifficulty difficulty;
        try {
            difficulty = LessonDifficulty.valueOf(request.difficulty().toUpperCase());
        } catch(IllegalArgumentException e) {
            log.warn("CreateLesson failed: difficulty entered did not match recognised difficulties.");
            throw new LessonDifficultyNotRecognisedException(request.difficulty());
        }

        Lesson newLesson = new Lesson(request.title(), request.description(), difficulty);
        return this.lessonGateway.createLesson(newLesson);
    }
}