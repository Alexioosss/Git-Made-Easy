package com.gitmadeeasy.infrastructure.dto.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;

import java.util.List;

public record LessonResponse(String lessonId, String title, String description,
                             DifficultyLevels difficulty, Integer lessonOrder, List<String> taskIds) {
    public LessonResponse(Lesson lesson) {
        this(lesson.getLessonId(), lesson.getTitle(), lesson.getDescription(),
                lesson.getDifficulty(), lesson.getLessonOrder(), lesson.getTaskIds());
    }
}