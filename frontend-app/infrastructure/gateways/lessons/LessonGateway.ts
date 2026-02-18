import { Lesson } from "@/types/lesson";

export interface LessonGateway {
    createLesson(title: string, description: string, difficulty: string): Promise<Lesson>;
    getById(lessonId: string): Promise<Lesson>;
    getAll(): Promise<Lesson[]>;
}