package com.gitmadeeasy.infrastructure.gateways.tasks.repositories;

import com.gitmadeeasy.infrastructure.gateways.tasks.TaskSchema;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    TaskSchema save(TaskSchema taskSchema);
    Optional<TaskSchema> findByLessonIdAndTaskId(String lessonId, String taskId);
    List<TaskSchema> findAllByLessonId(String lessonId);
    boolean existsById(String taskId);
    Integer findMaxTaskOrderByLessonId(String lessonId);
}