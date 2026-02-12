package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractJpaTaskAttemptRepository extends JpaRepository<TaskAttemptSchema, Long> {
    Optional<TaskAttemptSchema> findByUserIdAndTaskId(Long userId, Long taskId);
    @Query("""
            SELECT COUNT(tp) FROM TaskAttemptSchema tp INNER JOIN TaskSchema t ON t.id = tp.taskId
            WHERE tp.userId = :userId AND t.lessonId = :lessonId AND tp.status = 'COMPLETED'
            """)
    int countCompletedTasks(Long userId, Long lessonId);
}
