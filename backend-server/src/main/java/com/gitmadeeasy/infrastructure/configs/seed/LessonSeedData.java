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
                        "Use git add <file>. And don't forget the file extension type." +
                                "For this case, example.txt will do", 1, DifficultyLevels.EASY),
                new Task(commits.getLessonId(), "Commit changes", "Create a commit with a message",
                        "git commit -m 'Added first file'",
                        "Commit messages should be meaningful and descriptive. Let's say: 'Added first file'", 2, DifficultyLevels.EASY),
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
                new Task(branching.getLessonId(), "Create a new branch", "Create a new branch for your own work." +
                        "Let's call it 'my-new-branch'", "git branch my-new-branch",
                        "Specify the branch name after the keyword 'branch'", 1, DifficultyLevels.EASY),
                new Task(branching.getLessonId(), "Switch to a branch", "Move to the branch you just created",
                        "git checkout my-new-branch",
                        "The branch is called 'my-new-branch'", 2, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "Create and switch in one step",
                        "Create a new branch and switch to it immediately",
                        "git checkout -b feature-dashboard",
                        "Think of combining branch creation and switching into a single command." +
                                "The switching command appears first", 3, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "List all branches", "View all branches in your repository",
                        "git branch", "Use the command that shows all branches and highlights the current one.",
                        4, DifficultyLevels.MEDIUM),
                new Task(branching.getLessonId(), "Delete a branch", "Remove a branch you no longer need",
                        "git branch -d my-new-branch",
                        "Use the branch command with the delete flag to remove a branch safely." +
                                "Let's delete the branch 'my-new-branch'", 5, DifficultyLevels.MEDIUM)
        ));
        lessons.add(branching);

        Lesson merging = new Lesson("Merging & Conflict Resolution",
                "Learn how to combine changes from different branches and resolve conflicts.", DifficultyLevels.MEDIUM, 4);
        merging.setTasks(List.of(
                new Task(merging.getLessonId(), "Merge a branch",
                        "Combine changes from one branch into another. Let's merge into branch 'dashboard'",
                        "git merge feature-dashboard",
                        "Specify the branch that you want to merge into", 1, DifficultyLevels.MEDIUM),
                new Task(merging.getLessonId(), "View merge conflicts", "See files that have conflicts after a merge",
                        "git status",
                        "Git will mark conflicted files as 'both modified'; check these before committing.", 2, DifficultyLevels.MEDIUM),
                new Task(merging.getLessonId(), "Resolve a conflict", "Edit the conflicted files and mark as resolved",
                        "git add file", "Once conflicting files have been resolved, i.e. missing lines," +
                                "they are then pushed to the repository." +
                                "Let's just pretend that we just fixed a file conflict...", 3, DifficultyLevels.MEDIUM),
                new Task(merging.getLessonId(), "Finalize the merge", "Complete the merge after resolving conflicts",
                        "git commit",
                        "If conflicts were resolved manually, Git requires a commit to finalize the merge." +
                                "Recalling a previous task...", 4, DifficultyLevels.MEDIUM)
        ));
        lessons.add(merging);

        Lesson remote = new Lesson("Remote Repositories & Collaboration",
                "Learn how to work with remote repositories and collaborate with others.", DifficultyLevels.MEDIUM, 5);
        remote.setTasks(List.of(
                new Task(remote.getLessonId(), "Clone a repository",
                        "Create a local copy of a remote repository. Let's clone this remote repository: https://github.com/onaly/storage",
                        "git clone https://github.com/user/repo.git",
                        "Clone the repository using its public URL", 1, DifficultyLevels.MEDIUM),
                new Task(remote.getLessonId(), "Push a local branch to a remote repository",
                        "Push your local branch to the remote repository",
                        "git push main my-new-branch",
                        "Let's just say we did not delete the previous branch, so let's push that to the `main` branch of the remote repository",
                        2,
                        DifficultyLevels.MEDIUM)
        ));
        lessons.add(remote);

        return lessons;
    }
}