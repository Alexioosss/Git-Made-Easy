package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.lessons.LessonDifficulty;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "lessons")
public class LessonSchema {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    private String id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private LessonDifficulty difficulty;

    protected LessonSchema() {}

    public LessonSchema(String title, String description, LessonDifficulty difficulty) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getId() {
        return String.valueOf(id);
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

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        LessonSchema that = (LessonSchema) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}