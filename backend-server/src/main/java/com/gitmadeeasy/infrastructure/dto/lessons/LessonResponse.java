package com.gitmadeeasy.infrastructure.dto.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;

import java.util.List;

public class LessonResponse {
    public String lessonId;
    public String title;
    public String description;
    public DifficultyLevels difficulty;
    public Integer lessonOrder;
    public List<String> taskIds;

    public LessonResponse(Lesson lesson) {
        this.lessonId = lesson.getLessonId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.difficulty = lesson.getDifficulty();
        this.lessonOrder = lesson.getLessonOrder();
        this.taskIds = lesson.getTaskIds();
    }
}