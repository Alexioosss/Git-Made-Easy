package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetAllLessons;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController @RequestMapping("/lessons")
public class LessonController {
    private final CreateLesson createLesson;
    private final GetLessonById getLessonById;
    private final GetAllLessons getAllLessons;
    private static final Logger log = LoggerFactory.getLogger(LessonController.class);

    public LessonController(CreateLesson createLesson, GetLessonById getLessonById, GetAllLessons getAllLessons) {
        this.createLesson = createLesson;
        this.getLessonById = getLessonById;
        this.getAllLessons = getAllLessons;
    }

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody CreateLessonRequest createLessonRequest) {
        log.info("POST /lessons - Creating new lesson");
        Lesson createdLesson = this.createLesson.execute(createLessonRequest);
        log.info("Lesson created successfully. LessonID={}", createdLesson.getLessonId());
        return ResponseEntity.created(URI.create("/lessons/" + createdLesson.getLessonId())).body(createdLesson);
    }

    @GetMapping
    public ResponseEntity<List<Lesson>> getAll() {
        log.info("GET /lessons - Fetching all lessons");
        List<Lesson> allLessons = this.getAllLessons.execute();
        return ResponseEntity.ok(allLessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable("lessonId") String lessonId) {
        log.info("GET /lessons/{} - Fetching lesson by its id", lessonId);
        Lesson foundLesson = this.getLessonById.execute(lessonId);
        log.info("Lesson found successfully. LessonID={}", foundLesson.getLessonId());
        return ResponseEntity.ok(foundLesson);
    }
}