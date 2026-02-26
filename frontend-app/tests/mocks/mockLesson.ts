import { createMockTask } from "./mockTask";

export function createMockLesson(overrides = {}) {
    return {
        lessonId: "1",
        title: "Lesson Title",
        description: "Lesson Description",
        difficulty: "EASY",
        lessonOrder: 1,
        tasks: [
            createMockTask(),
            createMockTask({ taskId: "2" })
        ],
        taskIds: [],
        ...overrides
    };
}