import { Task } from "@/types/task";

export interface TaskGateway {
    getById(lessonId: string, taskId: string): Promise<Task>;
}