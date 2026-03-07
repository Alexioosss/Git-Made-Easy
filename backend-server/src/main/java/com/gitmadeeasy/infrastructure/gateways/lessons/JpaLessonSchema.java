package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.infrastructure.gateways.tasks.JpaTaskSchema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity @Table(name = "lessons")
public class JpaLessonSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING) private DifficultyLevels difficulty;
    private Integer lessonOrder;
    @Transient private List<JpaTaskSchema> tasks = new ArrayList<>();

    protected JpaLessonSchema() {}

    public JpaLessonSchema(String title, String description,
                           DifficultyLevels difficulty, Integer lessonOrder) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.lessonOrder = lessonOrder;
    }

    public JpaLessonSchema(String id, String title, String description,
                           DifficultyLevels difficulty, Integer lessonOrder) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.lessonOrder = lessonOrder;
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

    public List<JpaTaskSchema> getTasks() {
        return tasks;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTasks(List<JpaTaskSchema> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        JpaLessonSchema that = (JpaLessonSchema) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}