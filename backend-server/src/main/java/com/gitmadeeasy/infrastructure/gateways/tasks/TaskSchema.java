package com.gitmadeeasy.infrastructure.gateways.tasks;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tasks")
public class TaskSchema {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;

    protected TaskSchema() {}

    public TaskSchema(String lessonId, String title, String content, String expectedCommand, String hint) {
        this.lessonId = Long.parseLong(lessonId);
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getLessonId() {
        return String.valueOf(lessonId);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getExpectedCommand() {
        return expectedCommand;
    }

    public String getHint() {
        return hint;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        TaskSchema that = (TaskSchema) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}