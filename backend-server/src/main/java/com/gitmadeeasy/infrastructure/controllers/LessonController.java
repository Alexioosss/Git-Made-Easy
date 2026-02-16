package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/lessons")
public class LessonController {
    private final CreateLesson createLesson;
    private final GetLessonById getLessonById;
    private static final Logger log = LoggerFactory.getLogger(LessonController.class);

    public LessonController(CreateLesson createLesson, GetLessonById getLessonById) {
        this.createLesson = createLesson;
        this.getLessonById = getLessonById;
    }

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody CreateLessonRequest createLessonRequest) {
        log.info("POST /lessons - Creating new lesson");
        Lesson createdLesson = createLesson.execute(createLessonRequest);
        log.info("Lesson created successfully. LessonID= {}", createdLesson.getLessonId());
        return ResponseEntity.created(URI.create("/lessons/" + createdLesson.getLessonId())).body(createdLesson);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable("lessonId") String lessonId) {
        log.info("GET /lessons/{} - Fetching lesson by its id", lessonId);
        Lesson foundLesson = getLessonById.execute(lessonId);
        log.info("Lesson found successfully. LessonID= {}", foundLesson.getLessonId());
        return ResponseEntity.ok(foundLesson);
    }
}