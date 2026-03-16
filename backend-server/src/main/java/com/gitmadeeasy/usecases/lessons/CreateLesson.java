package com.gitmadeeasy.usecases.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import com.gitmadeeasy.usecases.lessons.exceptions.DifficultyLevelNotRecognisedException;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateLesson {
    private final LessonGateway lessonGateway;
    private static final Logger log = LoggerFactory.getLogger(CreateLesson.class);

    public CreateLesson(LessonGateway lessonGateway) {
        this.lessonGateway = lessonGateway;
    }

    public Lesson execute(CreateLessonRequest request) {
        // Ensure all the required fields are not missing, null or blank
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

        DifficultyLevels difficulty;
        try { difficulty = DifficultyLevels.valueOf(request.difficulty().toUpperCase()); }
        catch(IllegalArgumentException e) {
            log.warn("CreateLesson failed: difficulty entered did not match recognised difficulties.");
            throw new DifficultyLevelNotRecognisedException(request.difficulty());
        }

        // If the lesson order entered is valid, use it, otherwise, fetch the next lesson order from data store
        Integer lessonOrder = (request.lessonOrder() != null && request.lessonOrder() >= 0)
                ? request.lessonOrder() : this.lessonGateway.getNextLessonOrder();
        log.info("Lesson order has been produced for current lesson");

        // Create the lesson object with the request data
        Lesson newLesson = new Lesson(request.title(), request.description(), difficulty, lessonOrder);
        return this.lessonGateway.createLesson(newLesson);
    }
}