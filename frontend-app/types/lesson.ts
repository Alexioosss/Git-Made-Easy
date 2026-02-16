import { Task } from "./task";

export interface Lesson {
  lessonId: string;
  title: string;
  description: string;
  orderIndex: number;
  tasks: Task[];
}