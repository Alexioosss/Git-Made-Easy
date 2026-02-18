package com.gitmadeeasy.infrastructure.gateways.tasks;

public class FirebaseTaskSchema {
    private String id;
    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;
    private Integer taskOrder;
    private String taskDifficulty;

    public FirebaseTaskSchema() {}

    public FirebaseTaskSchema(
            String lessonId, String title, String content, String expectedCommand,
            String hint, Integer taskOrder, String taskDifficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
        this.taskDifficulty = taskDifficulty;
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

    public String getTaskDifficulty() { return taskDifficulty; }

    public void setId(String id) { this.id = id; }
}