import { LocalTaskProgress } from "@/infrastructure/persistence/localProgressData";
import { TaskProgress } from "@/types/taskProgress";

export interface TaskProgressGateway {
    recordTaskAttempt(lessonId: string, taskId: string, answer: string): Promise<TaskProgress>;
    getTaskProgress(lessonId: string, taskId: string): Promise<TaskProgress>;
    syncLocalProgress(lessonId: string, body: LocalTaskProgress[]): Promise<TaskProgress>;
}