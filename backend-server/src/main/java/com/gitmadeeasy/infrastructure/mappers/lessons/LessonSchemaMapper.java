package com.gitmadeeasy.infrastructure.mappers.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.infrastructure.gateways.lessons.FirebaseLessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;

public class LessonSchemaMapper {

    public JpaLessonSchema toJpaSchema(Lesson lesson) {
        JpaLessonSchema schema =  new JpaLessonSchema(
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getDifficulty(),
                lesson.getLessonOrder());
        if(lesson.getLessonId() != null) { schema.setId(lesson.getLessonId()); }
        return schema;
    }

    public Lesson fromJpaSchema(JpaLessonSchema schema) {
        return new Lesson(
                schema.getId(),
                schema.getTitle(),
                schema.getDescription(),
                schema.getDifficulty(),
                schema.getLessonOrder());
    }



    // ----- Firebase-Related Mapping ----- //

    public FirebaseLessonSchema toFirebaseSchema(Lesson lesson) {
        FirebaseLessonSchema schema = new FirebaseLessonSchema(
                lesson.getTitle(), lesson.getDescription(),
                lesson.getDifficulty(), lesson.getLessonOrder());
        if (lesson.getLessonId() != null) { schema.setId(lesson.getLessonId()); }
        schema.setTaskIds(lesson.getTaskIds());
        schema.setLongDescription(lesson.getLongDescription());
        return schema;
    }

    public Lesson fromFirebaseSchema(FirebaseLessonSchema schema) {
        Lesson lesson = new Lesson(
                schema.getId(), schema.getTitle(),
                schema.getDescription(), schema.getDifficulty(),
                schema.getLessonOrder());
        lesson.setTaskIds(schema.getTaskIds());
        lesson.setLongDescription(schema.getLongDescription());
        return lesson;
    }
}