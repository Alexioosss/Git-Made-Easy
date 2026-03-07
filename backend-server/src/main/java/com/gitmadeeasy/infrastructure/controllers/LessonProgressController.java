package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.GetAllLessonProgress;
import com.gitmadeeasy.usecases.lessonProgress.GetLessonProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController @RequestMapping("/lessons")
public class LessonProgressController {
    private final GetLessonProgress getLessonProgress;
    private final GetAllLessonProgress getAllLessonProgress;
    private static final Logger log = LoggerFactory.getLogger(LessonProgressController.class);

    public LessonProgressController(GetLessonProgress getLessonProgress, GetAllLessonProgress getAllLessonProgress) {
        this.getLessonProgress = getLessonProgress;
        this.getAllLessonProgress = getAllLessonProgress;
    }

    @GetMapping("/{lessonId}/progress")
    public ResponseEntity<LessonProgress> getLessonProgress(@PathVariable("lessonId") String lessonId,
                                                            Principal principal) {
        log.info("GET /lessons/{}/progress - Fetching lesson progress for authenticated user", lessonId);
        Optional<LessonProgress> progress = this.getLessonProgress.execute(principal.getName(), lessonId);
        log.info("Lesson progress found successfully");
        return progress.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/progress")
    public ResponseEntity<List<LessonProgress>> getAllLessonProgress(Principal principal) {
        log.info("GET /lessons/progress - Fetching all lesson progress for authenticated user");
        List<LessonProgress> progressList = this.getAllLessonProgress.execute(principal.getName());
        return ResponseEntity.ok(progressList);
    }
}