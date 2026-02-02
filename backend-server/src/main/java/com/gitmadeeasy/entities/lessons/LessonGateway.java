package com.gitmadeeasy.entities.lessons;

import java.util.Optional;

public interface LessonGateway {
    Lesson createLesson(Lesson newLesson);
    Optional<Lesson> getLessonById(String lessonId);
    boolean existsById(String lessonId);
}