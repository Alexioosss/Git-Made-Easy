package com.gitmadeeasy.infrastructure.configs.seed;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class LessonSeedData {

    public static List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();

        Lesson introLesson = new Lesson("Introduction to Git", "Learn the basics of Git",
                DifficultyLevels.EASY, 1);
        introLesson.setTasks(List.of(
                new Task(introLesson.getLessonId(), "Initialize a Git repository",
                        "Run the command to initialise Git.", "git init",
                        "Use git inside your project folder", 1, DifficultyLevels.EASY),
                new Task(introLesson.getLessonId(), "Check the current Git version", "Check your installed Git version.",
                        "git --version", "Try running --version in your terminal, and the Git version should show",
                        2, DifficultyLevels.EASY),
                new Task(introLesson.getLessonId(), "View repository status", "See what has changed so far",
                        "git status", "Use git status to view latest changes", 3, DifficultyLevels.EASY)
        ));
        lessons.add(introLesson);

        return lessons;
    }
}