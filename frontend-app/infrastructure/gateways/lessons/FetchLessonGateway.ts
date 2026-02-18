import { Lesson } from "@/types/lesson";
import { ApiClient } from "../ApiClient";
import { LessonGateway } from "./LessonGateway";
import { HttpMethods } from "../HttpMethods";

export class FetchLessonGateway implements LessonGateway {

    constructor(private apiClient: ApiClient) {}

    createLesson(title: string, description: string, difficulty: string): Promise<Lesson> {
        return this.apiClient.apiRequest("/lessons", HttpMethods.POST, { title, description, difficulty });
    }

    getById(lessonId: string): Promise<Lesson> {
        return this.apiClient.apiRequest(`/lessons/${lessonId}`, HttpMethods.GET);
    }

    getAll(): Promise<Lesson[]> {
        return this.apiClient.apiRequest("/lessons", HttpMethods.GET);
    }
}