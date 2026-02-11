package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import com.gitmadeeasy.infrastructure.gateways.lessons.repositories.LessonRepository;

import java.util.Optional;

public class ConcreteJpaLessonRepository implements LessonRepository {
    private final AbstractJpaLessonRepository jpa;

    public ConcreteJpaLessonRepository(AbstractJpaLessonRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public LessonSchema save(LessonSchema lessonSchema) {
        return this.jpa.save(lessonSchema);
    }

    @Override
    public Optional<LessonSchema> findById(String lessonId) {
        return this.jpa.findById(Long.valueOf(lessonId));
    }

    @Override
    public boolean existsById(String lessonId) {
        return this.jpa.existsById(Long.valueOf(lessonId));
    }

    @Override
    public void deleteAll() { this.jpa.deleteAll(); }
}