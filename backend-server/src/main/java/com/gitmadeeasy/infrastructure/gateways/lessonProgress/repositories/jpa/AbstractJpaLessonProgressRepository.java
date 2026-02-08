package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.LessonProgressSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractJpaLessonProgressRepository extends JpaRepository<LessonProgressSchema, Long> {
    Optional<LessonProgressSchema> findByUserIdAndLessonId(Long userId, Long lessonId);
}