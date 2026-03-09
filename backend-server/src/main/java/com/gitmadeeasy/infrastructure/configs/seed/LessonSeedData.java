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
        filesAndCommits.setLongDescription("""
                To do anything useful in Git, you first need one or more commits in your repository. A commit is created from the changes
                to one or more files in a repository, review the differences, add them to the index, create a new commit from the contents
                of the index, and repeat this cycle. Git's index is a staging area used to build up new commits. Rather than require all
                changes in the working tree to make up for the next commit, Git allows files to be added incrementally to the index.
                """); // McQuaid, M. (2014) Git in Practice
        lessons.add(filesAndCommits);

        Lesson branchingBasics = new Lesson("Branching Basics",
                "Learn how to create, switch, and manage branches in Git.", DifficultyLevels.MEDIUM, 3);
        branchingBasics.setTasks(List.of(
                new Task(branchingBasics.getLessonId(), "Create a new branch", "Create a new branch for your own work. " +
                        "Let's call it my-new-branch", "git branch my-new-branch",
                        "Specify the branch name after the keyword branch", 1, DifficultyLevels.EASY),
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
        branchingBasics.setLongDescription("""
                A branch in a version control system is an active parallel line of development (also called a codeline). They are used to isolate, separate,
                and gather different types of work. For example, branches can be used to prevent your current unfinished work on a feature in
                progress from interfering with the management of bug fixes, or to gather fixed for an older version of the developed software.
                A single Git repository can have an arbitrarily large number of branches. Moreover, with a distributed version control system, such as Git,
                there could be many repositories for a single project (known as forks or clones), some public and some private; each such repository will
                have their own local branches. This can be considered source branching. Each developer would have at least one private clone of the project's public repository to work in.
                """); // Narębski, J. (2024) Mastering Git : attain expert-level proficiency with Git by mastering distributed version control features. Second edition. Packt Publishing Ltd. Available at: https://www.oreilly.com/library/view/-/9781835086070/
        lessons.add(branchingBasics);

        Lesson mergingAndConflicts = new Lesson("Merging & Conflict Resolution",
                "Learn how to combine changes from different branches and resolve conflicts.", DifficultyLevels.MEDIUM, 4);
        mergingAndConflicts.setTasks(List.of(
                new Task(mergingAndConflicts.getLessonId(), "Merge a branch",
                        "Let's just say we did not delete the previous branch, so let's combine changes from one branch into another. Let's merge into branch 'my-new-branch'",
                        "git merge my-new-branch",
                        "Specify the branch that you want to merge into", 1, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "View merge conflicts", "After merging, check the status of the repository to check if any files have conflicts that need resolving.",
                        "git status",
                        "Files with conflicts will be marked as 'both modified'; check these before committing. Use this command to find files that need checking before completing the merge.", 2, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "Resolve a conflict", "Edit the conflicted files and mark as resolved",
                        "git add my-new-file", "Once conflicting files have been resolved, " +
                                "they are then pushed to the repository. Let's just pretend that we just fixed a file conflict... Let's stage the file 'my-new-file'", 3, DifficultyLevels.MEDIUM),
                new Task(mergingAndConflicts.getLessonId(), "Finalize the merge", "Complete the merge after resolving conflicts",
                        "git commit",
                        "If conflicts were resolved manually, Git requires a commit to finalize the merge. Recalling a previous task...", 4, DifficultyLevels.MEDIUM)
        ));
        mergingAndConflicts.setLongDescription("""
                Now that you have changes from other people in the remote-tracking branches, you need to combine them, perhaps also with your changes.
                Alternatively, your work on a new feature, created and performed on a separate topic branch, is now ready to be included in the long-lived development branch and made available to other people.
                Maybe you have created a bug fix and want to include it in all the long-lived graduation branches. In short, you want to join two divergent lines of development by integrating their changes.
                Git provides a few different methods to combine changes and variations of these methods. One of these methods is a merge operation, joining two lines of development with a two-parent commit.
                Another way to copy introduced work from one branch to another is via cherry-picking, which is creating a new commit with the same changeset on another line of development
                (this is sometimes necessary to use). Alternatively, you can reapply changes, transplanting one branch on top of another with rebase. In many cases, Git will be able to combine changes automatically.
                """); // Packt Publishing, Mastering Git - Second Edition (2024)
        lessons.add(mergingAndConflicts);

        Lesson remoteRepositories = new Lesson("Remote Repositories & Collaboration",
                "Learn how to work with remote repositories and collaborate with others.", DifficultyLevels.MEDIUM, 5);
        remoteRepositories.setTasks(List.of(
                new Task(remoteRepositories.getLessonId(), "Clone a repository",
                        "Create a local copy of a remote repository. Let's clone this remote repository: https://github.com/onaly/storage",
                        "git clone https://github.com/onaly/storage",
                        "Clone the repository using its public URL", 1, DifficultyLevels.MEDIUM),
                new Task(remoteRepositories.getLessonId(), "Push a local branch to a remote repository",
                        "Push your local branch to the remote repository", "git push origin my-new-branch",
                        "Let's push our local branch, my-new-branch, to the remote repository (origin). This updates the remote repository with your local changes.",
                        2, DifficultyLevels.MEDIUM)
        ));
        remoteRepositories.setLongDescription("""
                When collaborating on any project managed with Git, you will interact often with a constant set of other repositories.
                When cloning a repository, Git will create one remote for you—the origin remote, which stores information about where you cloned from—that is the
                origin of your copy of the repository (hence the name). You can use this remote to fetch updates. This is the default remote.
                """); // Packt Publishing, Mastering Git - Second Edition (2024)
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
        rebaseAndSquashing.setLongDescription("""
                Besides merging, Git supports an additional way to integrate changes from one branch into another – namely, the rebase operation.
                Like a merge, it deals with the changes since the point of divergence (at least, by default).
                However, while a merge creates a new commit by joining two branches, rebase takes the new commits from one branch
                (i.e., takes the commits since the divergence) and reapplies them on top of the other branch.
                With merge, you first switch to the branch to be merged and then use the merge command to select a branch to merge in.
                With rebase, it is a bit different. First, you select a branch to rebase (i.e., the changes to reapply)
                and then use the rebase command to select where to put it. In both cases, you first check out the branch to be modified,
                where a new commit or commits would be (a merge commit in the case of merging, and a replay of commits in the case of rebasing)
                """); // Packt Publishing, Mastering Git - Second Edition (2024)
        lessons.add(rebaseAndSquashing);

        return lessons;
    }
}