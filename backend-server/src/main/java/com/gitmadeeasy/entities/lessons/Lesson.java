package com.gitmadeeasy.entities.lessons;

import java.util.Objects;

public class Lesson {
    private String lessonId;
    private String title;
    private String description;
    private String difficulty;

    public Lesson(String lessonId, String title, String description, String difficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public Lesson(String title, String description, String difficulty) {
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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