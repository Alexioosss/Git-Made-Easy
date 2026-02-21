package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.firebase.FirebaseLessonRepository;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;

import java.util.List;
import java.util.Optional;

public class FirebaseLessonDatabaseGateway implements LessonGateway {
    private final FirebaseLessonRepository firebase;
    private final LessonSchemaMapper mapper;

    public FirebaseLessonDatabaseGateway(FirebaseLessonRepository firebase, LessonSchemaMapper mapper) {
        this.firebase = firebase;
        this.mapper = mapper;
    }

    @Override
    public Lesson createLesson(Lesson newLesson) {
        return this.mapper.fromFirebaseSchema(
                this.firebase.save(this.mapper.toFirebaseSchema(newLesson))
        );
    }

    @Override
    public Optional<Lesson> getLessonById(String lessonId) {
        return this.firebase.findById(lessonId).map(this.mapper::fromFirebaseSchema);
    }

    @Override
    public boolean existsById(String lessonId) {
        return this.firebase.existsById(lessonId);
    }

    @Override
    public List<Lesson> findAllLessons() {
        return this.firebase.findAll().stream().map(this.mapper::fromFirebaseSchema).toList();
    }

    @Override
    public Integer getNextLessonOrder() {
        return this.firebase.findMaxLessonOrder() + 1;
    }

    @Override
    public void updateTaskIds(String lessonId, List<String> taskIds) {
        this.firebase.updateTaskIds(lessonId, taskIds);
    }
}