package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessons.LessonSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractJpaLessonRepository extends JpaRepository<LessonSchema, Long> {
}