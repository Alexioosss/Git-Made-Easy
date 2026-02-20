import { LessonProgress, TaskProgress } from "./taskProgress";

export interface DashboardData {
    userId: string;
    firstName: string;
    lastName: string;
    lessonsProgress: LessonProgress[];
    tasksProgress: TaskProgress[];
}