package com.gitmadeeasy.infrastructure.gateways.tasks;

public class FirebaseTaskSchema {
    private String id;
    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;
    private Integer taskOrder;
    private String difficulty;

    public FirebaseTaskSchema() {}

    public FirebaseTaskSchema(
            String lessonId, String title, String content, String expectedCommand,
            String hint, Integer taskOrder, String difficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
        this.difficulty = difficulty;
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

    public String getDifficulty() { return difficulty; }

    public void setId(String id) { this.id = id; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}