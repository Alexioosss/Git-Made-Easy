package com.gitmadeeasy.infrastructure.mappers.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;

public class LessonProgressSchemaMapper {

    public LessonProgressSchema toSchema(LessonProgress entity) {
        return new LessonProgressSchema(
                entity.getUserId(),
                entity.getLessonId(),
                entity.getCurrentTaskProgressId(),
                entity.getCompletedTasksCount(),
                entity.getTotalTasksCount()
        );
    }

    public LessonProgress toEntity(LessonProgressSchema schema) {
        return new LessonProgress(
                schema.getLessonProgressId(),
                schema.getUserId(),
                schema.getLessonId(),
                schema.getCurrentTaskProgressId(),
                schema.getCompletedTasksCount(),
                schema.getTotalTasksCount()
        );
    }
}