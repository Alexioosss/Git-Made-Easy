package com.gitmadeeasy.infrastructure.gateways.lessons;

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
    private String difficulty;

    protected LessonSchema() {}

    public LessonSchema(String title, String description, String difficulty) {
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

    public String getDifficulty() {
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
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(difficulty, that.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, difficulty);
    }
}