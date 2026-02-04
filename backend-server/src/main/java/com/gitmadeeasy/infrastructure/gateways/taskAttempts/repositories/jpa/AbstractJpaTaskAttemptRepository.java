package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractJpaTaskAttemptRepository extends JpaRepository<TaskAttemptSchema, Long> {
    Optional<TaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId);
}