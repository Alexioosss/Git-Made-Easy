package com.gitmadeeasy.entities.lessonProgress;

import java.util.List;
import java.util.Optional;

public interface LessonProgressGateway {
    LessonProgress save(LessonProgress lessonProgress);
    Optional<LessonProgress> findByUserIdAndLessonId(String userId, String lessonId);
    List<LessonProgress> findAllByUserId(String userId);
}