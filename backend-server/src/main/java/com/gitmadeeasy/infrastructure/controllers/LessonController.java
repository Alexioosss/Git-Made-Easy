package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.lessons.CreateLesson;
import com.gitmadeeasy.usecases.lessons.dto.CreateLessonRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final CreateLesson createLesson;

    public LessonController(CreateLesson createLesson) {
        this.createLesson = createLesson;
    }

    @PostMapping("")
    public ResponseEntity<Void> createLesson(@RequestBody CreateLessonRequest createLessonRequest) {
        createLesson.execute(createLessonRequest);
        return ResponseEntity.ok().build();
    }
}