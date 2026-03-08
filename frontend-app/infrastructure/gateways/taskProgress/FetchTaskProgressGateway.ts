import { TaskProgress } from "@/types/taskProgress";
import { ApiClient } from "../ApiClient";
import { TaskProgressGateway } from "./TaskProgressGateway";
import { HttpMethods } from "../HttpMethods";
import { LocalTaskProgress } from "@/types/localProgressData";

export class FetchTaskProgressGateway implements TaskProgressGateway {

    constructor(private apiClient: ApiClient) {}

    recordTaskAttempt(lessonId: string, taskId: string, answer: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.POST, { input: answer });
    } 

    getTaskProgress(lessonId: string, taskId: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.GET);
    }

    getAllTaskProgress(): Promise<TaskProgress[]> {
        return this.apiClient.apiRequest(`/tasks/progress`, HttpMethods.GET);
    }

    syncLocalProgress(lessonId: string, body: LocalTaskProgress[]): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/progress/sync`, HttpMethods.POST, body);
    }
}