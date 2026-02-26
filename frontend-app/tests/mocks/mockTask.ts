import { Task } from "@/types/task";
import { DifficultyLevels } from "@/types/difficultyLevels";

export function createMockTask(overrides: Partial<Task> = {}): Task {
    return {
        taskId: "1",
        lessonId: "1",
        title: "Title",
        content: "Content",
        expectedCommand: "command",
        hint: "hint",
        taskOrder: 1,
        difficulty: DifficultyLevels.EASY,
        ...overrides
    };
}