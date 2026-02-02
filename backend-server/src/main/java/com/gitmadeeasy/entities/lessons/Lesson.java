package com.gitmadeeasy.entities.lessons;

import com.gitmadeeasy.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lesson {
    private String lessonId;
    private String title;
    private String description;
    private LessonDifficulty difficulty;
    private List<Task> tasks = new ArrayList<>();

    public Lesson(String lessonId, String title, String description, LessonDifficulty difficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public Lesson(String title, String description, LessonDifficulty difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LessonDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(LessonDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(title, lesson.title) &&
                Objects.equals(description, lesson.description) &&
                Objects.equals(difficulty, lesson.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, difficulty);
    }
}