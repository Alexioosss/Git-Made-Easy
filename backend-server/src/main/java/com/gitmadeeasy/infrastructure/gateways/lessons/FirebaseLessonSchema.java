package com.gitmadeeasy.infrastructure.gateways.lessons;

import com.gitmadeeasy.entities.enums.DifficultyLevels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseLessonSchema {
    private String id;
    private String title;
    private String description;
    private DifficultyLevels difficulty;
    private Integer lessonOrder;
    private List<String> taskIds = new ArrayList<>();
    private String longDescription;

    protected FirebaseLessonSchema() {}

    public FirebaseLessonSchema(String title, String description,
                                DifficultyLevels difficulty, Integer lessonOrder) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.lessonOrder = lessonOrder;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public DifficultyLevels getDifficulty() {
        return difficulty;
    }

    public Integer getLessonOrder() { return lessonOrder; }

    public List<String> getTaskIds() {
        return taskIds;
    }

    public String getLongDescription() { return longDescription; }

    public void setId(String id) { this.id = id; }

    public void setTaskIds(List<String> taskIds) { this.taskIds = taskIds; }

    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        FirebaseLessonSchema that = (FirebaseLessonSchema) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}