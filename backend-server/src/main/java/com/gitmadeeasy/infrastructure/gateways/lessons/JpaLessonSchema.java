package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "lessons")
public class JpaLessonSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING) private DifficultyLevels difficulty;
    @Transient private List<JpaTaskSchema> tasks = new ArrayList<>();

    protected JpaLessonSchema() {}

    public JpaLessonSchema(String title,
                           String description, DifficultyLevels difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public JpaLessonSchema(String id, String title,
                           String description, DifficultyLevels difficulty) {
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

    public List<JpaTaskSchema> getTasks() {
        return tasks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(DifficultyLevels difficulty) {
        this.difficulty = difficulty;
    }

    public void setTasks(List<JpaTaskSchema> tasks) {
        this.tasks = tasks;
    }
}