package com.gitmadeeasy.infrastructure.gateways.tasks.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository @Profile("test")
public interface JpaTaskRepository extends JpaRepository<JpaTaskSchema, String> {
    Optional<JpaTaskSchema> findByLessonIdAndId(String lessonId, String id);
    List<JpaTaskSchema> findAllByLessonId(String lessonId);

    @Query("SELECT COALESCE(MAX(t.taskOrder), 0) FROM JpaTaskSchema t WHERE t.lessonId= :lessonId")
    Integer findMaxTaskOrderByLessonId(String lessonId);
}