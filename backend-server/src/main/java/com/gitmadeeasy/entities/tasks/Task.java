package com.gitmadeeasy.entities.tasks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitmadeeasy.entities.enums.DifficultyLevels;

import java.util.Objects;

public class Task {
    private String taskId;
    private String lessonId;
    private final String title;
    private final String content;
    private final String expectedCommand;
    private final String hint;
    private final Integer taskOrder;
    private final DifficultyLevels difficulty;

    @JsonCreator
    public Task(@JsonProperty("title") String title,
                @JsonProperty("content") String content,
                @JsonProperty("expectedCommand") String expectedCommand,
                @JsonProperty("hint") String hint,
                @JsonProperty("taskOrder") Integer taskOrder,
                @JsonProperty("difficulty") DifficultyLevels difficulty) {
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
        this.difficulty = difficulty;
    }

    public Task(
            String taskId, String lessonId, String title, String content,
            String expectedCommand, String hint, Integer taskOrder, DifficultyLevels difficulty) {
        this.taskId = taskId;
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
        this.difficulty = difficulty;
    }

    public Task(
            String lessonId, String title, String content,
            String expectedCommand, String hint, Integer taskOrder, DifficultyLevels difficulty) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.expectedCommand = expectedCommand;
        this.hint = hint;
        this.taskOrder = taskOrder;
        this.difficulty = difficulty;
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

    public DifficultyLevels getDifficulty() { return difficulty; }

    public boolean isCorrectAnswer(String input) {
        return expectedCommand.equals(input);
    }

    public void setTaskId(String id) { this.taskId = id; }

    public void setLessonId(String lessonId) { this.lessonId = lessonId; }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", lessonId='" + lessonId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", expectedCommand='" + expectedCommand + '\'' +
                ", hint='" + hint + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
                Objects.equals(content, task.content) &&
                Objects.equals(expectedCommand, task.expectedCommand) &&
                Objects.equals(hint, task.hint) &&
                Objects.equals(difficulty, task.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, content, expectedCommand, hint, difficulty);
    }
}