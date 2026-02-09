package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/lessons/{lessonId}/progress")
public class LessonProgressController {
    private final GetLessonProgress getLessonProgress;

    public LessonProgressController(GetLessonProgress getLessonProgress) {
        this.getLessonProgress = getLessonProgress;
    }

    @GetMapping
    public ResponseEntity<LessonProgress> getLessonProgress(@PathVariable("lessonId") String lessonId,
                                                            Principal principal) {
        Optional<LessonProgress> progress = this.getLessonProgress.execute(principal.getName(), lessonId);
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}