package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;

public class LessonSchema {
    private String id;
    private String title;
    private String description;
    private LessonDifficulty difficulty;

    public LessonSchema() {}

    public LessonSchema(String title, String description, LessonDifficulty difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LessonDifficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public String toString() {
        return "LessonSchema{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}