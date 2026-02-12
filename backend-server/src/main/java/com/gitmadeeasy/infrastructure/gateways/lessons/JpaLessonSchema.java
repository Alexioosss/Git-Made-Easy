package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
public class JpaLessonSchema {

    @Id
    @Column(name = "lesson_id")
    private String lessonId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private LessonDifficulty difficulty;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaTaskSchema> tasks = new ArrayList<>();

    // JPA requires a no-arg constructor
    public JpaLessonSchema() {}

    public JpaLessonSchema(String lessonId, String title, String description, LessonDifficulty difficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

    // Getters
    public String getLessonId() {
        return lessonId;
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

    // Setters
    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
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
