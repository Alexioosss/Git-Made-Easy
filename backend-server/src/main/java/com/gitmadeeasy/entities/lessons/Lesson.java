package com.gitmadeeasy.entities.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lesson {
    private String lessonId;
    private final String title;
    private final String description;
    private final DifficultyLevels difficulty;
    private final Integer lessonOrder;
    private List<Task> tasks = new ArrayList<>();
    private List<String> taskIds = new ArrayList<>();

    public Lesson(String lessonId, String title, String description,
                  DifficultyLevels difficulty, Integer lessonOrder) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.lessonOrder = lessonOrder;
    }

    public Lesson(String title, String description, DifficultyLevels difficulty, Integer lessonOrder) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.lessonOrder = lessonOrder;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public DifficultyLevels getDifficulty() {
        return difficulty;
    }

    public Integer getLessonOrder() { return lessonOrder; }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<String> getTaskIds() { return taskIds; }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public void setTaskIds(List<String> taskIds) { this.taskIds = taskIds; }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", lessonOrder='" + lessonOrder + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(lessonId, lesson.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId);
    }
}