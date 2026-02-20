package com.gitmadeeasy.infrastructure.gateways.lessonProgress.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessonProgress.JpaLessonProgressSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository @Profile("test")
public interface JpaLessonProgressRepository extends JpaRepository<JpaLessonProgressSchema, String> {
    Optional<JpaLessonProgressSchema> findByUserIdAndLessonId(String userId, String lessonId);
    List<JpaLessonProgressSchema> findAllByUserId(String userId);
}