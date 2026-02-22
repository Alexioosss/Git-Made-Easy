import { TaskProgress } from "@/types/taskProgress";

export interface TaskProgressGateway {
    recordTaskAttempt(lessonId: string, taskId: string, answer: string): Promise<TaskProgress>;
    getTaskProgress(lessonId: string, taskId: string): Promise<TaskProgress>;
}