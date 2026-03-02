import { TaskProgress } from "@/types/taskProgress";
import { ApiClient } from "../ApiClient";
import { TaskProgressGateway } from "./TaskProgressGateway";
import { HttpMethods } from "../HttpMethods";
import { LocalTaskProgress } from "@/infrastructure/persistence/localProgressData";

export class FetchTaskProgressGateway implements TaskProgressGateway {

    constructor(private apiClient: ApiClient) {}

    recordTaskAttempt(lessonId: string, taskId: string, answer: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.POST, { input: answer });
    } 

    getTaskProgress(lessonId: string, taskId: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.GET);
    }

    getAllLessonProgress(): Promise<TaskProgress[]> {
        return this.apiClient.apiRequest(`/progress`, HttpMethods.GET);
    }

    syncLocalProgress(lessonId: string, body: LocalTaskProgress[]): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/progress/sync`, HttpMethods.POST, body);
    }
}