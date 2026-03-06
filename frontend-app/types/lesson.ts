import { Task } from "./task";

export interface Lesson {
  lessonId: string;
  title: string;
  description: string;
  difficulty: string;
  lessonOrder: number;
  taskIds: String[];
  tasks: Task[];
}