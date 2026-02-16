import { Lesson } from "@/types/lesson";
import { LessonProgress, TaskProgress } from "@/types/progress";

export const mockLessons: Lesson[] = [
  {
    lessonId: "lesson-1",
    title: "Getting Started with Git",
    description:
      "Learn the basics of Git version control. Initialize repositories, understand the staging area, and make your first commit.",
    orderIndex: 1,
    tasks: [
      {
        taskId: "task-1-1",
        title: "Initialize a Repository",
        description:
          "Create a new Git repository in the current directory. This is the first step in any Git project.",
        difficulty: "EASY",
        orderIndex: 1,
        hint: "Think about the command that creates a .git directory",
        expectedAnswer: "git init",
      },
      {
        taskId: "task-1-2",
        title: "Check Repository Status",
        description:
          "View the current status of your working directory and staging area to see which files have been modified or staged.",
        difficulty: "EASY",
        orderIndex: 2,
        hint: "This command shows the state of the working directory",
        expectedAnswer: "git status",
      },
      {
        taskId: "task-1-3",
        title: "Stage All Changes",
        description:
          "Add all modified and new files to the staging area, preparing them for the next commit.",
        difficulty: "EASY",
        orderIndex: 3,
        hint: "Use a dot to represent all files",
        expectedAnswer: "git add .",
      },
    ],
  },
  {
    lessonId: "lesson-2",
    title: "Branching and Merging",
    description:
      "Master the art of branching. Create, switch between, and merge branches to manage parallel development workflows.",
    orderIndex: 2,
    tasks: [
      {
        taskId: "task-2-1",
        title: "Create a New Branch",
        description:
          'Create a new branch called "feature" to start working on a new feature without affecting the main branch.',
        difficulty: "EASY",
        orderIndex: 1,
        hint: "Use the branch command with a name",
        expectedAnswer: "git branch feature",
      },
      {
        taskId: "task-2-2",
        title: "Switch to a Branch",
        description:
          'Switch your working directory to the "feature" branch you just created.',
        difficulty: "EASY",
        orderIndex: 2,
        hint: "There are two commands that can do this - one old, one new",
        expectedAnswer: "git checkout feature",
      },
      {
        taskId: "task-2-3",
        title: "Merge a Branch",
        description:
          'Merge the "feature" branch into your current branch, combining the changes.',
        difficulty: "MEDIUM",
        orderIndex: 3,
        hint: "Make sure you are on the branch you want to merge INTO",
        expectedAnswer: "git merge feature",
      },
      {
        taskId: "task-2-4",
        title: "Delete a Branch",
        description:
          'Delete the "feature" branch after it has been merged and is no longer needed.',
        difficulty: "MEDIUM",
        orderIndex: 4,
        hint: "Use a flag to delete",
        expectedAnswer: "git branch -d feature",
      },
    ],
  },
  {
    lessonId: "lesson-3",
    title: "Working with Remotes",
    description:
      "Connect to remote repositories. Push, pull, and fetch changes to collaborate with others effectively.",
    orderIndex: 3,
    tasks: [
      {
        taskId: "task-3-1",
        title: "Add a Remote",
        description:
          "Add a remote repository called 'origin' pointing to https://github.com/user/repo.git",
        difficulty: "MEDIUM",
        orderIndex: 1,
        hint: "Use the remote add command with a name and URL",
        expectedAnswer: "git remote add origin https://github.com/user/repo.git",
      },
      {
        taskId: "task-3-2",
        title: "Push to Remote",
        description:
          "Push your local main branch to the remote origin for the first time.",
        difficulty: "MEDIUM",
        orderIndex: 2,
        hint: "You need to set the upstream tracking reference",
        expectedAnswer: "git push -u origin main",
      },
      {
        taskId: "task-3-3",
        title: "Pull from Remote",
        description:
          "Fetch and integrate changes from the remote main branch into your current branch.",
        difficulty: "MEDIUM",
        orderIndex: 3,
        hint: "This command fetches and merges in one step",
        expectedAnswer: "git pull origin main",
      },
    ],
  },
  {
    lessonId: "lesson-4",
    title: "Advanced Git Techniques",
    description:
      "Dive into advanced Git features. Learn rebasing, cherry-picking, stashing, and interactive staging.",
    orderIndex: 4,
    tasks: [
      {
        taskId: "task-4-1",
        title: "Stash Your Changes",
        description:
          "Temporarily save your uncommitted changes so you can switch to another branch.",
        difficulty: "HARD",
        orderIndex: 1,
        hint: "This command saves changes on a stack",
        expectedAnswer: "git stash",
      },
      {
        taskId: "task-4-2",
        title: "Rebase onto Main",
        description:
          "Rebase your current branch onto main to create a linear commit history.",
        difficulty: "HARD",
        orderIndex: 2,
        hint: "This rewrites commit history to be linear",
        expectedAnswer: "git rebase main",
      },
      {
        taskId: "task-4-3",
        title: "Cherry-Pick a Commit",
        description:
          "Apply the changes from commit abc1234 to your current branch without merging.",
        difficulty: "HARD",
        orderIndex: 3,
        hint: "Pick a specific commit by its hash",
        expectedAnswer: "git cherry-pick abc1234",
      },
    ],
  },
];

export const mockTaskProgress: Record<string, TaskProgress> = {
  "task-1-1": {
    userId: "user-1",
    lessonId: "lesson-1",
    taskId: "task-1-1",
    completed: true,
    attempts: 1,
    lastAttemptDate: "2026-02-08T10:00:00Z",
  },
  "task-1-2": {
    userId: "user-1",
    lessonId: "lesson-1",
    taskId: "task-1-2",
    completed: true,
    attempts: 2,
    lastAttemptDate: "2026-02-08T10:15:00Z",
  },
  "task-1-3": {
    userId: "user-1",
    lessonId: "lesson-1",
    taskId: "task-1-3",
    completed: false,
    attempts: 1,
    lastAttemptDate: "2026-02-08T10:30:00Z",
  },
  "task-2-1": {
    userId: "user-1",
    lessonId: "lesson-2",
    taskId: "task-2-1",
    completed: true,
    attempts: 1,
    lastAttemptDate: "2026-02-08T11:00:00Z",
  },
};

export const mockLessonProgress: Record<string, LessonProgress> = {
  "lesson-1": {
    userId: "user-1",
    lessonId: "lesson-1",
    completedTasks: 2,
    totalTasks: 3,
    progressPercentage: 67,
  },
  "lesson-2": {
    userId: "user-1",
    lessonId: "lesson-2",
    completedTasks: 1,
    totalTasks: 4,
    progressPercentage: 25,
  },
};

export const mockUser = {
  id: "user-1",
  firstName: "Alex",
  lastName: "Developer",
  emailAddress: "alex@example.com",
};
