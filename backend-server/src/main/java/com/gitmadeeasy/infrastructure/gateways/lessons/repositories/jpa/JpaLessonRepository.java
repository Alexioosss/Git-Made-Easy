package com.gitmadeeasy.infrastructure.gateways.lessons.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.lessons.JpaLessonSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository @Profile("test")
public interface JpaLessonRepository extends JpaRepository<JpaLessonSchema, String> {
    @Query("SELECT COALESCE(MAX(l.lessonOrder), 0) FROM JpaLessonSchema l")
    Integer findMaxLessonOrder();
    List<JpaLessonSchema> findAllByOrderByLessonOrderAsc();
}