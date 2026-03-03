package com.gitmadeeasy.infrastructure.configs.seed;

import com.gitmadeeasy.entities.enums.DifficultyLevels;
import com.gitmadeeasy.entities.lessons.Lesson;
import com.gitmadeeasy.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class LessonSeedData {

    public static List<Lesson> getLessons() {
        List<Lesson> lessons = new ArrayList<>();

        Lesson introduction = new Lesson("Introduction to Git", "Learn the basics of Git", DifficultyLevels.EASY, 1);
        introduction.setTasks(List.of(
                new Task(introduction.getLessonId(), "Initialize a Git repository",
                        "Run the command to initialise Git", "git init",
                        "Use git inside your project folder", 1, DifficultyLevels.EASY),
                new Task(introduction.getLessonId(), "Check the current Git version", "Check your installed Git version",
                        "git --version", "Try running --version in your terminal, and the Git version should show",
                        2, DifficultyLevels.EASY),
                new Task(introduction.getLessonId(), "View repository status", "See what has changed so far",
                        "git status", "Use git status to view latest changes", 3, DifficultyLevels.EASY)
        ));
        introduction.setLongDescription("""
                Git is a distributed version control system used in software development to track changes made to source code.
                It enables multiple developers to collaborate independently on a single project by having separate local repositories,
                synchronized with the remote repository shared among them. Repositories hold a project's files and its history of changes.
                A commit in a repository is a snapshot of the project at a given point in time, distinguishable by a distinct hash and a commit message.
                Branches allow developers to work independently on different features or fixes. The default branch for stable code is usually 'main' (formerly master).
                """);
        lessons.add(introduction);

        Lesson filesAndCommits = new Lesson("Working with Files & Commits",
                "Learn how to stage, commit, and track file changes.", DifficultyLevels.EASY, 2);
        filesAndCommits.setTasks(List.of(
                new Task(filesAndCommits.getLessonId(), "Stage a file", "Add a file to the staging area",
                        "git add example.txt",
                        "Use git add <file>. And don't forget the file extension type." +
                                "For this case, replace <file> with example.txt", 1, DifficultyLevels.EASY),
                new Task(filesAndCommits.getLessonId(), "Commit changes", "Create a commit with a message",
                        "git commit -m 'Added first file'",
                        "Commit messages should be meaningful and descriptive. Let's give it a descriptive message." +
                                "Let's say: 'Added first file'", 2, DifficultyLevels.EASY),
                new Task(filesAndCommits.getLessonId(), "Stage all changes", "Stage all modified files",
                        "git add .", "Be careful when using add .", 3, DifficultyLevels.EASY),
                new Task(filesAndCommits.getLessonId(), "View commit history", "See previous commits",
                        "git log", "Use the command that shows a list, or log, of all past commits",
                        4, DifficultyLevels.EASY)
        ));
        lessons.add(filesAndCommits);

        Lesson branchingBasics = new Lesson("Branching Basics",
                "Learn how to create, switch, and manage branches in Git.", DifficultyLevels.MEDIUM, 3);
        branchingBasics.setTasks(List.of(
                new Task(branchingBasics.getLessonId(), "Create a new branch", "Create a new branch for your own work." +
                        "Let's call it 'my-new-branch'", "git branch my-new-branch",
                        "Specify the branch name after the keyword branch, and don't forget quotes", 1, DifficultyLevels.EASY),
                new Task(branchingBasics.getLessonId(), "Switch to a branch", "Move to the branch you just created",
                        "git checkout my-new-branch",
                        "The branch is called my-new-branch", 2, DifficultyLevels.MEDIUM),
                new Task(branchingBasics.getLessonId(), "Create and switch in one step",
                        "Create a new branch and switch to it immediately",
                        "git checkout -b my-new-branch",
                        "Think of combining branch creation and switching into a single command." +
                                "The switching command appears first. Branch is called my-new-branch", 3, DifficultyLevels.MEDIUM),
                new Task(branchingBasics.getLessonId(), "List all branches", "View all branches in your repository",
                        "git branch -a", "Use the command that shows ALL branches and highlights the current one.",
                        4, DifficultyLevels.MEDIUM),
                new Task(branchingBasics.getLessonId(), "Delete a branch", "Remove a branch you no longer need",
                        "git branch -d my-new-branch",
                        "Use the branch command with the delete flag to remove a branch safely." +
                                "Let's delete the branch my-new-branch", 5, DifficultyLevels.MEDIUM)
        ));
        lessons.add(branchingBasics);

        Lesson mergingAndConflicts = new Lesson("Merging & Conflict Resolution",
                "Learn how to combine changes from different branches and resolve conflicts.", DifficultyLevels.MEDIUM, 4);
        mergingAndConflicts.setTasks(List.of(
                new Task(mergingAndConflicts.getLessonId(), "Merge a branch",
                        "Let's just say we did not delete the previous branch, so let's combine changes from" +
                                "one branch into another. Let's merge into branch 'my-new-branch'",
                        "git merge my-new-branch",
                        "Specify the branch that you want to merge into", 1, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "View merge conflicts", "See files that have conflicts after a merge",
                        "git status",
                        "Git will mark conflicted files as 'both modified'; check these before committing." +
                                "Run the command to check the status of the repository", 2, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "Resolve a conflict", "Edit the conflicted files and mark as resolved",
                        "git add my-new-file", "Once conflicting files have been resolved; for example missing lines," +
                                "they are then pushed to the repository. Let's just pretend that we just fixed a file conflict..." +
                        "Let's push the file 'my-new-file'", 3, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "Finalize the merge", "Complete the merge after resolving conflicts",
                        "git commit",
                        "If conflicts were resolved manually, Git requires a commit to finalize the merge." +
                                "Recalling a previous task...", 4, DifficultyLevels.MEDIUM)
        ));
        lessons.add(mergingAndConflicts);

        Lesson remoteRepositories = new Lesson("Remote Repositories & Collaboration",
                "Learn how to work with remote repositories and collaborate with others.", DifficultyLevels.MEDIUM, 5);
        remoteRepositories.setTasks(List.of(
                new Task(remoteRepositories.getLessonId(), "Clone a repository",
                        "Create a local copy of a remote repository. Let's clone this remote repository: https://github.com/onaly/storage",
                        "git clone https://github.com/onaly/storage",
                        "Clone the repository using its public URL", 1, DifficultyLevels.MEDIUM),
                new Task(remoteRepositories.getLessonId(), "Push a local branch to a remote repository",
                        "Push your local branch to the remote repository", "git push main my-new-branch",
                        "Let's push our local branch to the `main` branch of the remote repository. Remote repository is called my-new-branch",
                        2, DifficultyLevels.MEDIUM)
        ));
        lessons.add(remoteRepositories);

        Lesson rebaseAndSquashing = new Lesson("Rebase & Commit Squashing",
                "Learn how to rebase branches and squash commits to maintain a clean history.", DifficultyLevels.HARD, 6);
        rebaseAndSquashing.setTasks(List.of(
                new Task(rebaseAndSquashing.getLessonId(), "Rebasing Branches",
                        "Rebase your feature branch onto main to keep history linear",
                        "git rebase main",
                        "Rebase your branch into branch 'main'", 1, DifficultyLevels.HARD),
                new Task(rebaseAndSquashing.getLessonId(), "Resolving Rebase Conflicts",
                        "Handle conflicts during rebase and continue the rebase process",
                        "git rebase --continue",
                        "Resolve conflicts manually, stage them, then rebase and Continue." +
                                "For this case, let's just say that we already resolved said conflicts. Just rebase and continue",
                        2, DifficultyLevels.HARD),
                new Task(rebaseAndSquashing.getLessonId(), "Force Push After Rebase",
                        "Force push your rebased branch to remote after rewriting history",
                        "git push --force",
                        "Be cautious with force pushing, especially on shared branches.", 3, DifficultyLevels.HARD)
        ));
        lessons.add(rebaseAndSquashing);

        return lessons;
    }
}