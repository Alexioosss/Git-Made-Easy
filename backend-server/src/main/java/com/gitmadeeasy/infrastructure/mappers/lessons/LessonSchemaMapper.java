package com.gitmadeeasy.infrastructure.mappers.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;

public class LessonSchemaMapper {

    public LessonSchema toSchema(Lesson lesson) {
        return new LessonSchema(
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getDifficulty()
        );
    }

    public Lesson toEntity(LessonSchema lessonSchema) {
        return new Lesson(
                lessonSchema.getId(),
                lessonSchema.getTitle(),
                lessonSchema.getDescription(),
                lessonSchema.getDifficulty()
        );
    }
}