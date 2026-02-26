import { LessonProgress } from "@/types/taskProgress";

export function createMockLessonProgress(overrides: Partial<LessonProgress> = {}): LessonProgress {
    return {
        lessonProgressId: "lp-1",
        userId: "u-1",
        lessonId: "l-1",
        currentTaskProgressId: "tp-1",
        completedTasksCount: 2,
        totalTasksCount: 5,
        ...overrides
    };
}