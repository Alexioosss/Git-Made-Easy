import { Task } from "@/types/task";
import { TaskGateway } from "./TaskGateway";
import { ApiClient } from "../ApiClient";
import { HttpMethods } from "../HttpMethods";

export class FetchTaskGateway implements TaskGateway {

    constructor(private apiClient: ApiClient) {}

    getById(lessonId: string, taskId: string): Promise<Task> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/tasks/${taskId}`, HttpMethods.GET, undefined, { cache: "force-cache" });
    }
}