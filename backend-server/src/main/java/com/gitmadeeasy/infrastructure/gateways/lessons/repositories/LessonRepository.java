package com.gitmadeeasy.infrastructure.gateways.lessons.repositories;

import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;

import java.util.Optional;

public interface LessonRepository {
    LessonSchema save(LessonSchema lessonSchema);
    Optional<LessonSchema> findById(String lessonId);
    boolean existsById(String lessonId);
}