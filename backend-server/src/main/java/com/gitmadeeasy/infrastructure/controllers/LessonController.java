package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.infrastructure.dto.lessons.LessonResponse;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetAllLessons;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.GetNextLesson;
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
    private final GetNextLesson getNextLesson;
    private static final Logger log = LoggerFactory.getLogger(LessonController.class);

    public LessonController(CreateLesson createLesson, GetLessonById getLessonById,
                            GetAllLessons getAllLessons, GetNextLesson getNextLesson) {
        this.createLesson = createLesson;
        this.getLessonById = getLessonById;
        this.getAllLessons = getAllLessons;
        this.getNextLesson = getNextLesson;
    }

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody CreateLessonRequest createLessonRequest) {
        log.info("POST /lessons - Creating new lesson");
        Lesson createdLesson = this.createLesson.execute(createLessonRequest);
        log.info("Lesson created successfully. Lesson ID {}", createdLesson.getLessonId());
        return ResponseEntity.created(URI.create("/lessons/" + createdLesson.getLessonId())).body(createdLesson);
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAll() {
        log.info("GET /lessons - Fetching all lessons");
        List<Lesson> allLessons = this.getAllLessons.execute();
        List<LessonResponse> lessonResponse = allLessons.stream().map(LessonResponse::new).toList();
        return ResponseEntity.ok(lessonResponse);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable("lessonId") String lessonId) {
        log.info("GET /lessons/{} - Fetching lesson by its id", lessonId);
        Lesson foundLesson = this.getLessonById.execute(lessonId);
        log.info("Lesson found successfully. Lesson ID {}", foundLesson.getLessonId());
        return ResponseEntity.ok(foundLesson);
    }

    @GetMapping("/{lessonId}/next")
    public ResponseEntity<Lesson> getNextLesson(@PathVariable("lessonId") String lessonId) {
        log.info("GET /lessons/{}/next - Fetching next lesson of lesson {}", lessonId, lessonId);
        Lesson foundLesson = this.getNextLesson.execute(lessonId);
        if(foundLesson == null) {
            log.info("Next lesson not found for lesson {}", lessonId);
            return ResponseEntity.ok().build();
        }
        log.info("Next lesson found successfully. Lesson ID {}", foundLesson.getLessonId());
        return ResponseEntity.ok(foundLesson);
    }
}