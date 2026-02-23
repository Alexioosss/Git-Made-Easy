package com.gitmadeeasy.infrastructure.configs.seed;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class LessonSeedData {

    public static List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();

        Lesson intro = new Lesson("Introduction to Git", "Learn the basics of Git", DifficultyLevels.EASY, 1);
        intro.setTasks(List.of(
                new Task(intro.getLessonId(), "Initialize a Git repository",
                        "Run the command to initialise Git", "git init",
                        "Use git inside your project folder", 1, DifficultyLevels.EASY),
                new Task(intro.getLessonId(), "Check the current Git version", "Check your installed Git version",
                        "git --version", "Try running --version in your terminal, and the Git version should show",
                        2, DifficultyLevels.EASY),
                new Task(intro.getLessonId(), "View repository status", "See what has changed so far",
                        "git status", "Use git status to view latest changes", 3, DifficultyLevels.EASY)
        ));
        lessons.add(intro);

        Lesson commits = new Lesson("Working with Files & Commits",
                "learn how to stage, commit, and track file changes.", DifficultyLevels.EASY, 2);
        commits.setTasks(List.of(
                new Task(commits.getLessonId(), "Stage a file", "Add a file to the staging area",
                        "git add example.txt",
                        "Use git add <file>. And don't forget the file extension type", 1, DifficultyLevels.EASY),
                new Task(commits.getLessonId(), "Commit changes", "Create a commit with a message",
                        "git commit -m \"Add example file\"", "Commit messages should be meaningful and descriptive",
                        2, DifficultyLevels.EASY),
                new Task(commits.getLessonId(), "Stage all changes", "Stage all modified files",
                        "git add .", "Be careful when using add .", 3, DifficultyLevels.EASY),
                new Task(commits.getLessonId(), "View commit history", "See previous commits",
                        "git log", "Use the command that shows a list of all past commits",
                        4, DifficultyLevels.EASY)
        ));
        lessons.add(commits);

        Lesson branching = new Lesson("Branching Basics",
                "Learn how to create, switch, and manage branches in Git.", DifficultyLevels.MEDIUM, 3);
        branching.setTasks(List.of(
                new Task(branching.getLessonId(), "Create a new branch", "Create a new branch for your own work",
                        "git branch my-new-branch",
                        "Use the command that lists and creates branches, followed by the new branch name.",
                        1, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "Switch to a branch", "Move to the branch you just created",
                        "git checkout my-new-branch",
                        "Use the command that changes your current branch to another one.", 2, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "Create and switch in one step",
                        "Create a new branch and switch to it immediately",
                        "git checkout -b feature-dashboard",
                        "Think of combining branch creation and switching into a single command.", 3, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "List all branches", "View all branches in your repository",
                        "git branch", "Use the command that shows all branches and highlights the current one.",
                        4, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "Delete a branch", "Remove a branch you no longer need",
                        "git branch -d feature-login",
                        "Use the branch command with the delete flag to remove a branch safely.", 5, DifficultyLevels.MEDIUM)
        ));
        lessons.add(branching);

        return lessons;
    }
}