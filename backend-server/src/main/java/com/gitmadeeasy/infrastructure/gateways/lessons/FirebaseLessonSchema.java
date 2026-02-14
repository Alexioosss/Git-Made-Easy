package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;

import java.util.ArrayList;
import java.util.List;

public class FirebaseLessonSchema {
    private String id;
    private String title;
    private String description;
    private LessonDifficulty difficulty;
    private List<String> taskIds = new ArrayList<>();

    protected FirebaseLessonSchema() {}

    public FirebaseLessonSchema(String title,
                                String description, LessonDifficulty difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public FirebaseLessonSchema(String id, String title,
                                String description, LessonDifficulty difficulty) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
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

    public List<String> getTasks() {
        return taskIds;
    }

    public void setId(String id) { this.id = id; }
}