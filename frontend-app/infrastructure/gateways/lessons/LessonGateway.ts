import { Lesson } from "@/types/lesson";
import { Task } from "@/types/task";

export interface LessonGateway {
    createLesson(title: string, description: string, difficulty: string): Promise<Lesson>;
    getById(lessonId: string): Promise<Lesson>;
    getAllLessons(): Promise<Lesson[]>;
    getTasksForLesson(lessonId: string): Promise<Task[]>;
}