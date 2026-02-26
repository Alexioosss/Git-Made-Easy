import { TaskProgress } from "@/types/taskProgress";

export function createMockTaskProgress(overrides: Partial<TaskProgress> = {}): TaskProgress {
    return {
        taskProgressId: "tp-1",
        userId: "u-1",
        taskId: "t-1",
        lessonId: "l-1",
        taskTitle: "Task Title",
        status: "IN_PROGRESS",
        attempts: 1,
        lastInput: "git",
        lastError: "Incorrect answer",
        startedAt: "2024-01-01T10:00:00Z",
        completedAt: "",
        ...overrides
    };
}