package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractJpaLessonRepository extends JpaRepository<JpaLessonSchema, Long> {
}