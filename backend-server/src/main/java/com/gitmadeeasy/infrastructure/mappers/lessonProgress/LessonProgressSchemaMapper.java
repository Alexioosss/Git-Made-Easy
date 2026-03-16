package com.gitmadeeasy.infrastructure.mappers.lessonProgress;

import com.gitmadeeasy.entities.lessonProgress.LessonProgress;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.FirebaseLessonProgressSchema;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressSchema;

public class LessonProgressSchemaMapper {

    public JpaLessonProgressSchema toJpaSchema(LessonProgress lessonProgress) {
        JpaLessonProgressSchema schema =  new JpaLessonProgressSchema(lessonProgress.getUserId(), lessonProgress.getLessonId(),
                lessonProgress.getCurrentTaskProgressId(), lessonProgress.getCompletedTasksCount(),lessonProgress.getTotalTasksCount());
        if(lessonProgress.getLessonProgressId() != null) { schema.setId(lessonProgress.getLessonProgressId()); }
        return schema;
    }

    public LessonProgress fromJpaSchema(JpaLessonProgressSchema schema) {
        return new LessonProgress(schema.getId(), schema.getUserId(), schema.getLessonId(),
                schema.getCurrentTaskProgressId(), schema.getCompletedTasksCount(), schema.getTotalTasksCount());
    }



    // ----- Firebase-Related Mapping ----- //

    public FirebaseLessonProgressSchema toFirebaseSchema(LessonProgress lessonProgress) {
        FirebaseLessonProgressSchema schema = new FirebaseLessonProgressSchema(
                lessonProgress.getUserId(), lessonProgress.getLessonId(),
                lessonProgress.getCurrentTaskProgressId(), lessonProgress.getCompletedTasksCount(),
                lessonProgress.getTotalTasksCount());
        schema.setId(lessonProgress.getLessonProgressId());

        return schema;
    }

    public LessonProgress fromFirebaseSchema(FirebaseLessonProgressSchema schema) {
        return new LessonProgress(
                schema.getId(), schema.getUserId(), schema.getLessonId(),
                schema.getCurrentTaskProgressId(), schema.getCompletedTasksCount(),
                schema.getTotalTasksCount());
    }
}