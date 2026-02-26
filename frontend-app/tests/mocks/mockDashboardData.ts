import { DashboardData } from "@/types/dashboard";
import { createMockTaskProgress } from "./mockTaskProgress";

export function createMockDashboardData(overrides: Partial<DashboardData> = {}): DashboardData {
    return {
        userId: "1",
        firstName: "John",
        lastName: "Doe",
        lessons: [
            {
                lessonId: "l-1",
                title: "Lesson 1",
                lessonOrder: 1,
                completedTasksCount: 2,
                totalTasksCount: 5
            },
            {
                lessonId: "l-2",
                title: "Lesson 2",
                lessonOrder: 2,
                completedTasksCount: 0,
                totalTasksCount: 4
            }
        ],
        tasksProgress: [
            createMockTaskProgress({ taskProgressId: "tp-1", lessonId: "l-1" }),
            createMockTaskProgress({ taskProgressId: "tp-1", lessonId: "l-2" })
        ],
        ...overrides
    };
}