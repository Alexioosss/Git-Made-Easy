package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbstractJpaTaskRepository extends JpaRepository<TaskSchema, Long> {
    Optional<TaskSchema> findByLessonIdAndId(Long lessonId, Long Id);
    List<TaskSchema> findAllByLessonId(Long lessonId);
}