package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.lessons.LessonGateway;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa.JpaLessonRepository;
import com.gitmadeeasy.infrastructure.mappers.lessons.LessonSchemaMapper;

import java.util.List;
import java.util.Optional;

public class JpaLessonDatabaseGateway implements LessonGateway {
    private final JpaLessonRepository jpa;
    private final LessonSchemaMapper lessonSchemaMapper;

    public JpaLessonDatabaseGateway(JpaLessonRepository jpa, LessonSchemaMapper lessonSchemaMapper) {
        this.jpa = jpa;
        this.lessonSchemaMapper = lessonSchemaMapper;
    }

    @Override
    public Lesson createLesson(Lesson newLesson) {
        return this.lessonSchemaMapper.fromJpaSchema(this.jpa.save(this.lessonSchemaMapper.toJpaSchema(newLesson)));
    }

    @Override
    public Optional<Lesson> getLessonById(String lessonId) {
        return this.jpa.findById(lessonId).map(this.lessonSchemaMapper::fromJpaSchema);
    }

    @Override
    public boolean existsById(String lessonId) { return this.jpa.existsById(lessonId); }

    @Override
    public List<Lesson> findAllLessons() {
        return this.jpa.findAll().stream().map(this.lessonSchemaMapper::fromJpaSchema).toList();
    }
}