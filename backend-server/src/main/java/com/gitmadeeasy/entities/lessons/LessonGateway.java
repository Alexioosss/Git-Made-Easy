package com.gitmadeeasy.entities.lessons;

import java.util.List;
import java.util.Optional;

public interface LessonGateway {
    Lesson createLesson(Lesson newLesson);
    Optional<Lesson> getLessonById(String lessonId);
    boolean existsById(String lessonId);
    List<Lesson> findAllLessons();
    Integer getNextLessonOrder();
    void updateTaskIds(String lessonId, List<String> taskIds);
}