import { LessonProgress } from "@/types/taskProgress";

export interface LessonProgressGateway {
    getLessonProgress(lessonId: string): Promise<LessonProgress>;
}