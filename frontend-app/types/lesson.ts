import { Task } from "./task";

export interface Lesson {
  lessonId: string;
  title: string;
  description: string;
  difficulty: string;
  lessonOrder: number;
  tasks: Task[];
}