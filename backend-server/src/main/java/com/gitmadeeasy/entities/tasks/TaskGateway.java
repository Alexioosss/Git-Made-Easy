package com.gitmadeeasy.entities.tasks;

import java.util.List;
import java.util.Optional;

public interface TaskGateway {
    Task createTask(Task newTask);
    Optional<Task> getTaskByLessonIdAndTaskId(String lessonId, String taskId);
    List<Task> getTasksByLessonId(String lessonId);
    boolean existsById(String taskId);
    int getNextTaskOrderForLesson(String lessonId);
    int countTasksInLesson(String lessonId);
}