import { TaskProgress } from "@/types/taskProgress";
import { ApiClient } from "../ApiClient";
import { TaskProgressGateway } from "./TaskProgressGateway";
import { HttpMethods } from "../HttpMethods";

export class FetchTaskProgressGateway implements TaskProgressGateway {

    constructor(private apiClient: ApiClient) {}

    recordTaskAttempt(lessonId: string, taskId: string, answer: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.POST);
    }

    getTaskProgress(lessonId: string, taskId: string): Promise<TaskProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}/progress`, HttpMethods.GET);
    }

    getAllLessonProgress(): Promise<TaskProgress[]> {
        return this.apiClient.apiRequest(`/progress`, HttpMethods.GET);
    }
}