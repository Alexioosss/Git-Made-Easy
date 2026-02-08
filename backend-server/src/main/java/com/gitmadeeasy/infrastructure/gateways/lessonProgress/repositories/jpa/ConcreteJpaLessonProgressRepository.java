package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;
import com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.LessonProgressRepository;

import java.util.Optional;

public class ConcreteJpaLessonProgressRepository implements LessonProgressRepository {
    private final AbstractJpaLessonProgressRepository jpa;

    public ConcreteJpaLessonProgressRepository(AbstractJpaLessonProgressRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public LessonProgressSchema save(LessonProgressSchema lessonProgressSchema) {
        return this.jpa.save(lessonProgressSchema);
    }

    @Override
    public Optional<LessonProgressSchema> findByUserIdAndLessonId(String userId, String lessonId) {
        return this.jpa.findByUserIdAndLessonId(Long.valueOf(userId), Long.valueOf(lessonId));
    }
}