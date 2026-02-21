import { LessonProgress, TaskProgress } from "./taskProgress";

export interface LessonSummary {
    lessonId: string;
    title: string;
    lessonOrder: number;
    completedTasksCount: number;
    totalTasksCount: number;
}

export interface DashboardData {
    userId: string;
    firstName: string;
    lastName: string;
    lessons: LessonSummary[];
    tasksProgress: TaskProgress[];
}