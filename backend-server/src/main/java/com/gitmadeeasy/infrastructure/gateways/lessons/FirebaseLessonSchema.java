package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;

import java.util.ArrayList;
import java.util.List;

public class FirebaseLessonSchema {
    private String id;
    private String title;
    private String description;
    private DifficultyLevels difficulty;
    private Integer lessonOrder;
    private List<String> taskIds = new ArrayList<>();

    protected FirebaseLessonSchema() {}

    public FirebaseLessonSchema(String title, String description,
                                DifficultyLevels difficulty, Integer lessonOrder) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public FirebaseLessonSchema(String id, String title, String description,
                                DifficultyLevels difficulty, Integer lessonOrder) {
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

    public DifficultyLevels getDifficulty() {
        return difficulty;
    }

    public Integer getLessonOrder() { return lessonOrder; }

    public List<String> getTasks() {
        return taskIds;
    }

    public void setTaskIds(List<String> taskIds) { this.taskIds = taskIds; }

    public void setId(String id) { this.id = id; }
}