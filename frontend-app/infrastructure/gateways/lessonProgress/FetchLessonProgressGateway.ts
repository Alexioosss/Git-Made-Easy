import { LessonProgress } from "@/types/taskProgress";
import { ApiClient } from "../ApiClient";
import { LessonProgressGateway } from "./LessonProgressGateway";
import { HttpMethods } from "../HttpMethods";

export class FetchLessonProgressGateway implements LessonProgressGateway {

    constructor(private apiClient: ApiClient) {}

    getLessonProgress(lessonId: string): Promise<LessonProgress> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}/progress`, HttpMethods.GET);
    }
}