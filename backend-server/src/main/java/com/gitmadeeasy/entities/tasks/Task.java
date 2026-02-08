package com.gitmadeeasy.entities.tasks;

import java.util.Objects;

public class Task {
    private String taskId;
    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;
    private Integer taskOrder;

    public Task(String taskId, String lessonId, String title, String content,
                String expectedCommand, String hint, Integer taskOrder) {
        this.taskId = taskId;
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
    }

    public Task(String lessonId, String title, String content,
                String expectedCommand, String hint, Integer taskOrder) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
    }

    public String getTaskId() {
        return taskId;
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

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setExpectedCommand(String expectedCommand) {
        this.expectedCommand = expectedCommand;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setTaskOrder(Integer taskOrder) {
        this.taskOrder = taskOrder;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", expectedCommand='" + expectedCommand + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
                Objects.equals(content, task.content) &&
                Objects.equals(expectedCommand, task.expectedCommand) &&
                Objects.equals(hint, task.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, expectedCommand, hint);
    }
}