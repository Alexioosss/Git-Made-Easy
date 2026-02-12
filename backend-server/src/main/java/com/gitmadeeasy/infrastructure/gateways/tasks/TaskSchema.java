package com.gitmadeeasy.infrastructure.gateways.tasks;

public class TaskSchema {

    private String id;
    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;
    private Integer taskOrder;

    public TaskSchema() {}

    public TaskSchema(String lessonId, String title, String content, String expectedCommand, String hint, Integer taskOrder) {
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

    public void setId(String id) {
        this.id = id;
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
}