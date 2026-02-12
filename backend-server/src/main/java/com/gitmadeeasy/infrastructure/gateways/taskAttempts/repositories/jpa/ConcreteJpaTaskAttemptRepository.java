package com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.jpa;

import com.gitmadeeasy.domain.models.TaskAttempt;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.TaskAttemptSchema;
import com.gitmadeeasy.infrastructure.gateways.taskAttempts.repositories.TaskAttemptRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public class ConcreteJpaTaskAttemptRepository implements TaskAttemptRepository {

    private final AbstractJpaTaskAttemptRepository jpaRepository;

    public ConcreteJpaTaskAttemptRepository(AbstractJpaTaskAttemptRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TaskAttemptSchema> findByUserIdAndTaskId(String userId, String taskId) {
        return jpaRepository.findByUserIdAndTaskId(userId, taskId);
    }

    @Override
    public TaskAttemptSchema save(TaskAttemptSchema taskAttemptSchema) {
        return jpaRepository.save(taskAttemptSchema);
    }

    @Override
    public int countCompletedTasks(String userId, String lessonId) {
        return jpaRepository.countCompletedTasks(Long.valueOf(userId), Long.valueOf(lessonId));
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
