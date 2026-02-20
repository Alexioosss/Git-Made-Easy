package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository @Profile("test")
public interface JpaTaskAttemptRepository extends JpaRepository<JpaTaskAttemptSchema, String> {
    Optional<JpaTaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId);
    @Query("""
            SELECT COUNT(tp) FROM JpaTaskAttemptSchema tp JOIN tp.task t
            WHERE tp.userId = :userId AND t.lessonId = :lessonId AND tp.status = 'COMPLETED'
            """)
    int countCompletedTasks(String userId, String lessonId);
    List<JpaTaskAttemptSchema> findAllByUserId(String userId);
}