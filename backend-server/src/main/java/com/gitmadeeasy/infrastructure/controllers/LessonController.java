package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.GetLessonById;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final CreateLesson createLesson;
    private final GetLessonById getLessonById;

    public LessonController(CreateLesson createLesson, GetLessonById getLessonById) {
        this.createLesson = createLesson;
        this.getLessonById = getLessonById;
    }

    @PostMapping("")
    public ResponseEntity<Lesson> createLesson(@RequestBody CreateLessonRequest createLessonRequest) {
        Lesson newLesson = createLesson.execute(createLessonRequest);
        return ResponseEntity.ok(newLesson);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable("lessonId") String lessonId) {
        Lesson foundLesson = getLessonById.execute(lessonId);
        return ResponseEntity.ok(foundLesson);
    }
}