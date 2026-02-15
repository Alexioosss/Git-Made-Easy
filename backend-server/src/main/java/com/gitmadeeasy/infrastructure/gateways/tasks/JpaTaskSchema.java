package com.gitmadeeasy.infrastructure.gateways.tasks;

import com.gitmadeeasy.infrastructure.gateways.taskAttempts.JpaTaskAttemptSchema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "tasks")
public class JpaTaskSchema {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;
    private Integer taskOrder;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<JpaTaskAttemptSchema> attempts = new ArrayList<>();

    protected JpaTaskSchema() {}

    public JpaTaskSchema(String lessonId, String title, String content,
                         String expectedCommand, String hint, Integer taskOrder) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
    }

    public String getId() {
        return id;
    }

    public String getLessonId() {
        return lessonId;
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

    public Integer getTaskOrder() {
        return taskOrder;
    }

    public void setId(String id) { this.id = id; }
}