package com.gitmadeeasy.usecases.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.entities.lessonProgress.LessonProgressGateway;

import java.util.Optional;

public class GetLessonProgress {
    private final LessonProgressGateway lessonProgressGateway;

    public GetLessonProgress(LessonProgressGateway lessonProgressGateway) {
        this.lessonProgressGateway = lessonProgressGateway;
    }

    public Optional<LessonProgress> execute(String userId, String lessonId) {
        return this.lessonProgressGateway.findByUserIdAndLessonId(userId, lessonId);
    }
}