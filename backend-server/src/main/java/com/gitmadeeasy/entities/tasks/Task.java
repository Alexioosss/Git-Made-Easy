package com.gitmadeeasy.entities.tasks;

import java.util.Objects;

public class Task {
    private String taskId;
    private String lessonId;
    private String title;
    private String content;
    private String expectedCommand;
    private String hint;

    public Task(String taskId, String lessonId, String title, String content,
                String expectedCommand, String hint) {
        this.taskId = taskId;
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
    }

    public Task(String lessonId, String title, String content,
                String expectedCommand, String hint) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpectedCommand() {
        return expectedCommand;
    }

    public void setExpectedCommand(String expectedCommand) {
        this.expectedCommand = expectedCommand;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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