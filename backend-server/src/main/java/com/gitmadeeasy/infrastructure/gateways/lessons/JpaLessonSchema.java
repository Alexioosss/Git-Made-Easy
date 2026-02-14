package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "lessons")
public class JpaLessonSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private LessonDifficulty difficulty;
    @Transient private List<JpaTaskSchema> tasks = new ArrayList<>();

    protected JpaLessonSchema() {}

    public JpaLessonSchema(String title,
                           String description, LessonDifficulty difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    public JpaLessonSchema(String id, String title,
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

    public void setDifficulty(LessonDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setTasks(List<JpaTaskSchema> tasks) {
        this.tasks = tasks;
    }
}