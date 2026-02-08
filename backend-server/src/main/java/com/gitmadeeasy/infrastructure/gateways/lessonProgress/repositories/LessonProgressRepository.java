package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;

import java.util.Optional;

public interface LessonProgressRepository {
    LessonProgressSchema save(LessonProgressSchema lessonProgressSchema);
    Optional<LessonProgressSchema> findByUserIdAndLessonId(String userId, String lessonId);
}