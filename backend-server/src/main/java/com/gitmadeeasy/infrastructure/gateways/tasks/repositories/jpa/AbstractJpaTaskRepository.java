package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AbstractJpaTaskRepository extends JpaRepository<TaskSchema, Long> {
    Optional<TaskSchema> findByLessonIdAndId(Long lessonId, Long Id);
    List<TaskSchema> findAllByLessonId(Long lessonId);

    @Query("SELECT COALESCE(MAX(t.taskOrder), 0) FROM TaskSchema t WHERE t.lessonId= :lessonId")
    Integer findMaxTaskOrderByLessonId(Long lessonId);
}